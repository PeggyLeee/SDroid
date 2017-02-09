package util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import javax.persistence.Column;
import javax.persistence.Entity;

public class ResultSetMapper<T> {

	@SuppressWarnings("unchecked")
	public List<T> mapRersultSetToObject(ResultSet rs, Class outputClass) {
		List<T> outputList = null;
		try {
			register();
			// make sure resultset is not null
			if (rs != null) {
				// check if outputClass has 'Entity' annotation
				if (outputClass.isAnnotationPresent(Entity.class)) {
					// get the resultset metadata
					ResultSetMetaData rsmd = rs.getMetaData();
					// get all the attributes of outputClass
					Field[] fields = outputClass.getDeclaredFields();
					while (rs.next()) {
						T bean = (T) outputClass.newInstance();
						for (int _iterator = 0; _iterator < rsmd
								.getColumnCount(); _iterator++) {
							// getting the SQL column name
							String columnName = rsmd
									.getColumnName(_iterator + 1);
							// reading the value of the SQL column
							Object columnValue = rs.getObject(_iterator + 1);
							// iterating over outputClass attributes to check if
							// any attribute has 'Column' annotation with
							// matching 'name' value
							for (Field field : fields) {
								if (field.isAnnotationPresent(Column.class)) {
									Column column = field
											.getAnnotation(Column.class);
									if (column.name().equalsIgnoreCase(
											columnName)
											&& columnValue != null) {
										BeanUtils.setProperty(bean,
												field.getName(), columnValue);

										break;
									}
								}
							}
						}
						if (outputList == null) {
							outputList = new ArrayList<T>();
						}
						outputList.add(bean);
					}

				} else {
					// throw some error
				}
			} else {
				return null;
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return outputList;
	}

	private void register() {
		ConvertUtils.register(new Converter() {

			@Override
			public Object convert(Class type, Object value) {

				if (value == null) {
					return null;
				}
				try {
					Date date = null;
					SimpleDateFormat df = new SimpleDateFormat(
							"yyyy-MM-dd");
					date = df.parse(value.toString());
					return (T) date;
				} catch (ParseException e) {
					throw new RuntimeException(e);
				}
			}
		}, Date.class);
	}

}
