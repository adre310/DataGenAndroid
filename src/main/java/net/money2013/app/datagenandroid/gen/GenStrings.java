/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.money2013.app.datagenandroid.gen;

import iae.home.datagenerator.datamodel.ResStrings;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import net.money2013.app.datagenandroid.GlobalSettings;
import net.money2013.app.datagenandroid.model.DataModel;
import net.money2013.app.datagenandroid.model.FieldModel;
import net.money2013.app.datagenandroid.model.ObjectModel;
import net.money2013.app.datagenandroid.model.ViewColumn;
import net.money2013.app.datagenandroid.model.ViewModel;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 *
 * @author aisaev
 */
public class GenStrings {
    private final DataModel dataModel;

    public GenStrings(DataModel dataModel) {
        this.dataModel = dataModel;
    }
    
    public void gen() throws Exception {
        ResStrings resStrings=new ResStrings();
        String outResFileName=Paths.get(GlobalSettings.OUT_DIR, "strings_db.xml").toString();
        for(ObjectModel objectModel : dataModel.getObjects()) {
            //String om_name=objectModel.getName().toLowerCase();
            for(FieldModel fieldModel : objectModel.getFields()) {
                if(!resStrings.getStrings().containsKey(fieldModel.getName().toLowerCase()))
                    resStrings.getStrings().put(fieldModel.getName().toLowerCase(), fieldModel.getName());
            }
        }
        
        for(ViewModel viewModel : dataModel.getViewList()) {
            //String om_name=viewModel.getName().toLowerCase();
            for(ViewColumn viewColumn : viewModel.getColumns()) {
                if(!resStrings.getStrings().containsKey(viewColumn.getName().toLowerCase()))
                    resStrings.getStrings().put(viewColumn.getName().toLowerCase(), viewColumn.getName());
            }
        }
        
        Serializer serializer = new Persister();
        serializer.write(resStrings, new File(outResFileName));
    }
}
