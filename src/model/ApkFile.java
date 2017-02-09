package model;

public class ApkFile
{
    /**
     * App id
     */
    private String appId;
    
    /**
     * Blob download path
     */
    private String downloadPath;
    
    /**
     * Market DA
     */
    private String marketDA;
    
    /**
     * Constructor
     * 
     * @param appId
     * @param downloadPath
     * @param marketDA
     */
    public ApkFile(String appId, String downloadPath, String marketDA)
    {
        this.appId = appId;
        this.downloadPath = downloadPath;
        this.marketDA = marketDA;
    }
    
    /**
     * Get app id
     * 
     * @return
     */
    public String getAppId()
    {
        return appId;
    }
    
    /**
     * Get download path
     * 
     * @return
     */
    public String getDownloadPath()
    {
        return downloadPath;
    }
    
    /**
     * Get market da
     * 
     * @return
     */
    public String getMarketDA()
    {
        return marketDA;
    }
}
