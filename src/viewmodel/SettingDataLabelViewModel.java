package viewmodel;

import java.util.List;

import model.DataLabel;
import model.Label;
import model.Policy;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

import util.LogInfo;
import dao.DataLabelDao;
import dao.LabelDao;
import dao.PolicyDao;

public class SettingDataLabelViewModel {

	// Model
	Label label;
	DataLabel dataLabel;
	// Dao
	private LabelDao dlDao;
	private PolicyDao pDao;
	private DataLabelDao datalblDao;
	// ListView
	private List<Label> labels;
	private List<DataLabel> datalabels;
	//UI
	@Wire("#fileName")
	private org.zkoss.zul.Label fileName;

	@Init
	public void init() {
		
		label = new Label();
		dataLabel= new DataLabel();
		dlDao = new LabelDao();
		pDao = new PolicyDao();
		datalblDao = new DataLabelDao();
		
		labels = new ListModelList<Label>();
		datalabels = new ListModelList<DataLabel>();

		loadData();
	}

	public void loadData() {
		
		getDataLabelList();
	}
	
	/**
	 * 功能: 當頁面初始化後，載入清單資料
	 * */
	public void getDataLabelList() {
		//資料標籤
		datalabels.clear();
		List<DataLabel> dataList = datalblDao.getAllList();
		if (dataList != null) {
			for (DataLabel label : dataList) {
				Label l = dlDao.getByLabelId(label.getLabel());
				label.setLabel(l.getLabel());
				datalabels.add(label);
			}
		}
		//標籤清單
		labels.clear();
		List<Label> dList = dlDao.getAllList();
		if (dList != null) {
			for (Label label : dList) {
				labels.add(label);
			}
		}
	}

	/**
	 * 功能: 新增資料標籤
	 * */
	@Command
	public void insertDataLabel() {
		if(!StringUtils.isBlank(fileName.getValue()) && !StringUtils.isBlank(label.getLabelId())){
			dataLabel.setLabel(label.getLabelId());
			dataLabel.setFileName(fileName.getValue());
			
			datalblDao.insert(dataLabel);
			Messagebox.show("新增成功");
			getDataLabelList();
		}else {
			Messagebox.show("資料標籤，不可為空。");
		}
		
	}
	/**
	 * 功能: 刪除資料標籤
	 * */
	@Command
	public void removeDataLabel(@BindingParam("mStr") final String id) {
		datalblDao.removeById(id);
		Messagebox.show("DataLabel刪除成功");
		getDataLabelList();
	}
	
	
	/**
	 * 功能: 新增標籤
	 * */
	@Command
	public void insertLabel(@BindingParam("mStr") String label) {
		if (!StringUtils.isBlank(label)) {
			dlDao.insert(label);
			Messagebox.show("新增成功");
			getDataLabelList();
		} else {
			Messagebox.show("標籤制定，不可為空。");
		}
	}

	/**
	 * 功能: 刪除標籤
	 * */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void removeLabel(@BindingParam("mStr") final String id) {
		final Label dl = dlDao.getById(id);
		final List<Policy> list = pDao.getListByDataLabel(dl.getLabelId());
		if (list != null) {
			Messagebox.show("此筆Label已有Matched Policy，是否確定刪除?", "Question",
					Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
					new EventListener() {
						@Override
						public void onEvent(Event e) throws Exception {
							if (Messagebox.ON_OK.equals(e.getName())) {
								for (Policy p : list) {
									pDao.removeById(p.getId().toString());
								}
								datalblDao.removeByLabel(dl.getLabelId());
								dlDao.removeById(id);
								Messagebox.show("Label刪除成功");
								getDataLabelList();
							} else if (Messagebox.ON_CANCEL.equals(e.getName())) {
								return;
							}
						}
					});
		} else {
			datalblDao.removeByLabel(dl.getLabelId());
			dlDao.removeById(id);
			Messagebox.show("Label刪除成功");
			getDataLabelList();
		}
	}

	/**
	 * 讀取UI fileName必備Function
	 * */
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view){
	    Selectors.wireComponents(view, this, false);
	}
	
	
	/**
	 * Getter & Setter
	 * */
	public void setLabel(Label label) {
		this.label = label;
	}

	public Label getLabel() {
		return label;
	}

	public List<Label> getLabels() {
		return labels;
	}

	public DataLabel getDataLabel() {
		return dataLabel;
	}

	public List<DataLabel> getDatalabels() {
		return datalabels;
	}

}
