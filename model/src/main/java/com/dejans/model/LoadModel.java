package com.dejans.model;

import java.io.File;
import java.io.IOException;

public class LoadModel {

    TreeMapYamlParse tMYP;

    // dirModel - it's directory (apsolute or relative path) where it can find yml files for model
    public LoadModel(String dirModel) throws IOException {

        File fileName = new File(dirModel + File.separator + "domains.yml");

        tMYP = new TreeMapYamlParse(fileName);

        fileName = new File(dirModel + File.separator + "model.yml");
        tMYP.addConfiguration(fileName);

        fileName = new File(dirModel + File.separator + "databases.yml");
        tMYP.addConfiguration(fileName);

        fileName = new File(dirModel + File.separator + "api.yml");
        tMYP.addConfiguration(fileName);
    }

    public TreeMapYamlParse getTMYP () {
        return tMYP;
    }
}