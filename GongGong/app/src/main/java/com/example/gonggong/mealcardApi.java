package com.example.gonggong;

import android.os.AsyncTask;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class mealcardApi extends AsyncTask<Void, Void, NodeList> {

    private String url;

    public mealcardApi(String url){
        this.url = url;
    }

    @Override
    protected NodeList doInBackground(Void... voids) {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        Document doc = null;
        try {
            doc = dBuilder.parse(url);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("item");

        return nList;
    }
    @Override
   protected void onPostExecute(NodeList str){
        super.onPostExecute(str);
   }
}

