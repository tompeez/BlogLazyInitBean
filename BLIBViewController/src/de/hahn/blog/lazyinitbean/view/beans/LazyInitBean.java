package de.hahn.blog.lazyinitbean.view.beans;

import javax.annotation.PostConstruct;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.servlet.ServletContext;

import oracle.adf.model.BindingContext;
import oracle.adf.share.logging.ADFLogger;

import oracle.binding.AttributeBinding;
import oracle.binding.BindingContainer;

public class LazyInitBean {
    private static transient final ADFLogger LOGGER = ADFLogger.createADFLogger(LazyInitBean.class);

    private String myName;

    public void setMyName(String myName) {
        LOGGER.info("set data: " + myName);
        this.myName = myName;
    }

    public String getMyName() {
        // lazy init the data only when it's null
        //  This method deferes the init of the data until the getter is called in the UI.
        if (myName == null) {
            LOGGER.info("init data through layz init");
            initData();
        }
        return myName;
    }

    public LazyInitBean() {
        LOGGER.info("LazyInitBean: c'tor");
    }

    @PostConstruct
    public void postconstructMethod() {
        LOGGER.info("PostConstruct Called!");
        // init everything here which can be done quickly and is needed to init UI components before showing them
        // or call initData() from here
    }

    private void initData() {
        LOGGER.info("data intialized");
        // this method inits the beans attributes (only one here)!
        myName = "just init myself";
        //Get ServlerContexct
        FacesContext ctx = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) ctx.getExternalContext().getContext();
        // get the binding container
        BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();

        // get an ADF attributevalue from the ADF page definitions
        AttributeBinding attr = (AttributeBinding) bindings.getControlBinding("myTestValue1");
        if (attr != null) {
            String old = (String) attr.getInputValue();
            attr.setInputValue("NEW DEFAULT VALUE");
            LOGGER.info("LazyInitBean: setnew default value to 'NEW DEFAULT VALUE' old: " + old);
        } else {
            LOGGER.info("LazyInitBean: bindings not present!");
        }
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
        resetData();
    }
}
