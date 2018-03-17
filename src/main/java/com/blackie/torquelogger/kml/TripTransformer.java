package com.blackie.torquelogger.kml;

import com.blackie.torquelogger.data.item.LogDetail;
import com.blackie.torquelogger.data.item.LogItem;
import com.blackie.torquelogger.data.item.TripItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TripTransformer {
    private final TripItem myTrip;

    public TripTransformer (final TripItem trip) {
        myTrip = trip;
    }

    public Document getKML () {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }

        Document doc = docBuilder.newDocument();

        Element root = doc.createElement("kml");
        doc.appendChild(root);

        Element documentNode = doc.createElement("Document");
        root.appendChild(documentNode);

        Element nameNode = doc.createElement("name");
        nameNode.setTextContent("Trippy");
        documentNode.appendChild(nameNode);

        Element open = doc.createElement("open");
        nameNode.setTextContent("1");
        documentNode.appendChild(open);

        Element placemark = doc.createElement("Placemark");
        documentNode.appendChild(placemark);

        Element lineString = doc.createElement("LineString");
        placemark.appendChild(lineString);

        Element coords = doc.createElement("coordinates");
        coords.setTextContent(createCoordsFromTrip(myTrip));
        lineString.appendChild(coords);

        return doc;
    }

    private String createCoordsFromTrip (TripItem trip) {
        StringBuilder sb = new StringBuilder();
        //make a space separated list of coords from the trip's logs
        List<LogItem> logs = trip.getLogs();

        Collections.sort(logs);
        long lastCaptured = 0;
        for (LogItem li : logs) {
            //TODO replace this with a more sophisticated averaging algorithm
            //So average the entries coinciding with the last three seconds, write those to the path. This should result
            //in a cleaner plot
            if (li.getTs().getTime() - lastCaptured > 250) {
                //Skip all entries that are less than 3 seconds apart.
                lastCaptured = li.getTs().getTime();
                sb.append(li.getLongitude()).append(",").append(li.getLat()).append(" ");
            }
        }

        return sb.toString();
    }
}
