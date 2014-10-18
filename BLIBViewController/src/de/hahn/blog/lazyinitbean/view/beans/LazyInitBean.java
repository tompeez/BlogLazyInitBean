package de.hahn.blog.lazyinitbean.view.beans;

import javax.faces.event.ActionEvent;

import oracle.adf.share.logging.ADFLogger;

public class LazyInitBean {
    private static transient final ADFLogger LOGGER = ADFLogger.createADFLogger(LazyInitBean.class);

    private String myName;

    public void setMyName(String myName) {
        LOGGER.info("set data: " + myName);
        this.myName = myName;
    }

    public String getMyName() {
        // lazy init the data only when it's null
        if (myName == null) {
            LOGGER.info("init data through layz init");
            initData();
        }
        return myName;
    }

    public LazyInitBean() {
        LOGGER.info("LazyInitBean: c'tor");
    }

    private void initData() {
        LOGGER.info("data intialized");
        // this method inits the beans attributes (only one here)!
        myName = "just init myself";
    }

    /**
     * Force the init of the beans attributes
     */
    public void resetData() {
        LOGGER.info("LazyInitBean: reset!");
        // setting the myName to null causes a re initialization
        myName = null;
        // you can call initData() here too;
        initData();
    }

    public void buttonListener(ActionEvent actionEvent) {
        LOGGER.info("Action initData");
        initData();
    }
}
