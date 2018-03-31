package com.dejans.ValidateAndException;

com.dejans.ValidateAndException.ModelDefinitionTree;

public class ValidateByDefinition {
    private ModelDefinitionTree model = null;
    private static ValidateByDefinition instance = null;

    // this is Singleton class
    private static ValidateByDefinition getInstance() {
        if (instance == null) {
            instance = new ValidateByDefinition();
        }
        return instance;
    }

    protected ValidateByDefinition() {
    }

    public static void setModel(ModelDefinitionTree model) {
        getInstance().setModelInternal(model);
    }

    private void setModelInternal(ModelDefinitionTree model) {
        this.model = model;
    }

    private void lengthValidate(String object, String item, int length) throws Error, Exception {
        ModelDefinitionTree.PropertiesValidate lval = model.propertyValidate("length", object, item);
        if (lval.validate) {
            if (length > Integer.parseInt(lval.valueProperty)) {

                ExceptionHandlings.throwException(lval.errorcode, lval.errormessage, lval.show);
            }
        }
    }

    private void requiredValidate(String object, String item, Object value) throws Error, Exception {
        boolean bool = model.getItemRequired(object, item);
        if (bool && value == null) {
            ModelDefinitionTree.PropertiesValidate lval = model.propertyValidate("required", object, item);
            ExceptionHandlings.throwException(lval.errorcode, lval.errormessage, lval.show);            
        }
    }

    public static void setValidate(String object, String item, String type, Object value) throws Exception {
        getInstance().setValidateInternal(object, item, type, value);
    }

    private void setValidateInternal(String object, String item, String type, Object value) throws Exception {
        System.out.println("USAO U SET VALIDATE");
        System.out.println("USAO U SET VALIDATE " + object + "." + item);
        System.out.println("USAO U SET VALIDATE");
        System.out.println("USAO U SET VALIDATE");
        try {
            if (type.equals("String")) {
                if (value != null && ((String) value).length() > 0) {
                    lengthValidate(object, item, ((String) value).length());

                }
            }
            requiredValidate(object, item, value);

        } catch (Exception e) {
            ExceptionHandlings.catchHandlings(e);

        } catch (Error e) {
            ExceptionHandlings.catchHandlings(e);
        }

    }
}