package com.example.jeju_makcha;

import android.os.AsyncTask;

import com.example.jeju_makcha.autosearch.Poi;
import com.example.jeju_makcha.autosearch.TMapSearchInfo;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Created by KJH on 2017-09-06.
 */

public class AutoCompleteParse extends AsyncTask<String, Void, ArrayList<SearchEntity>>
{
    private final String TMAP_API_KEY = "rubJGfeh4T5jt7ILof4ob6bCp6g3P3c141jghFt4";
    private final int SEARCH_COUNT = 20;  // minimum is 20
    private ArrayList<SearchEntity> mListData;
    private RecyclerViewAdapter mAdapter;

    public AutoCompleteParse(RecyclerViewAdapter adapter) {
        this.mAdapter = adapter;
        mListData = new ArrayList<SearchEntity>();
    }

    @Override
    protected ArrayList<SearchEntity> doInBackground(String... word) {
        return getAutoComplete(word[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<SearchEntity> autoCompleteItems) {
        mAdapter.setData(autoCompleteItems);
        mAdapter.notifyDataSetChanged();
    }

    public ArrayList<SearchEntity> getAutoComplete(String word) {
        try {
            String encodeWord = URLEncoder.encode(word, "UTF-8");
            URL acUrl = new URL(
                    "https://apis.openapi.sk.com/tmap/pois?version=1&searchKeyword=" +
                            encodeWord + "&searchType=all&searchtypCd=A&reqCoordType=WGS84GEO&resCoordType=WGS84GEO&page=1&count=" + SEARCH_COUNT + "&multiPoint=N&poiGroupYn=N"
            );

            HttpURLConnection acConn = (HttpURLConnection) acUrl.openConnection();
            acConn.setRequestProperty("Accept", "application/xml");  // Set the Accept header to request XML
            acConn.setRequestProperty("appKey", TMAP_API_KEY);

            BufferedReader reader = new BufferedReader(new InputStreamReader(acConn.getInputStream()));

            StringBuilder xmlResponse = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                xmlResponse.append(line);
            }
            reader.close();

            mListData.clear();

            // Parse the XML response
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(xmlResponse.toString()));
            Document doc = builder.parse(inputSource);

            NodeList poiList = doc.getElementsByTagName("poi");
            for (int i = 0; i < poiList.getLength(); i++) {
                Element poiElement = (Element) poiList.item(i);
                String name = getNodeValue(poiElement, "name");
                String upperAddrName = getNodeValue(poiElement, "upperAddrName");
                String middleAddrName = getNodeValue(poiElement, "middleAddrName");
                String lowerAddrName = getNodeValue(poiElement, "lowerAddrName");
                String detailAddrName = getNodeValue(poiElement, "detailAddrName");
                String fullAddr = upperAddrName + " " + middleAddrName + " " + lowerAddrName + " " + detailAddrName;

                mListData.add(new SearchEntity(name, fullAddr));
            }

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

        return mListData;
    }

    private String getNodeValue(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            return node.getTextContent();
        }
        return "";
    }
}