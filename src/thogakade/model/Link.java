/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thogakade.model;

/**
 *
 * @author saranga
 */
public class Link {

    /**
     * @return the completeFactor
     */
    public double getCompleteFactor() {
        return completeFactor;
    }

    /**
     * @param completeFactor the completeFactor to set
     */
    public void setCompleteFactor(double completeFactor) {
        this.completeFactor = completeFactor;
    }

    private Integer id;
    private Integer type;
    private String link;
    private String status;
    private String downloadtime;
    private double completeFactor;

    public void setDownloadtime(String downloadtime) {
        this.downloadtime = downloadtime;
    }

    public String getDownloadtime() {
        return downloadtime;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }
}
