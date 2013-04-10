///*
// * To change this template, choose Tools | Templates and open the template in
// * the editor.
// */
//package com.spectre.seriesfiletype;
//
//import org.openide.cookies.CloseCookie;
//import org.openide.cookies.OpenCookie;
//import org.openide.loaders.OpenSupport;
//import org.openide.windows.CloneableTopComponent;
//
///**
// * @author Kyle Williams
// */
//public class SeriesOpenSupport extends OpenSupport implements OpenCookie, CloseCookie{
//    
//    public SeriesOpenSupport(SeriesDataObject.Entry entry){
//        super(entry);
//    }
//    
//    @Override
//    protected CloneableTopComponent createCloneableTopComponent(){
//        SeriesDataObject dobj = (SeriesDataObject)entry.getDataObject();        
////        FlairEditorTopComponent dc = FlairEditorTopComponent.getInstance();   
////        dc.getExplorerManager().setRootContext(dobj.getNodeDelegate());        
////        dc.setDisplayName(dobj.getName());
//        return null;
//    }
//    
//}
