package com.blackie.torquelogger.api;

import com.blackie.torquelogger.data.item.TripItem;
import com.blackie.torquelogger.data.store.TripStore;
import com.blackie.torquelogger.kml.TripTransformer;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;

import javax.ws.rs.core.MediaType;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

@RestController

public class TripController {
    private TripStore tripStore;

    public TripController () {
        this.tripStore = new TripStore();
    }

    @RequestMapping(value="/trip/{id}", method=RequestMethod.GET)
    public TripItem addLog(@PathVariable int id) {
        TripItem ti = tripStore.getTrip(id);
        return ti;
    }

    @RequestMapping(value="/trip/{id}/kml", produces = MediaType.TEXT_XML)
    public @ResponseBody String getKml (@PathVariable int id) {
        //TODO git this in the transformer class
        TripItem ti = tripStore.getTrip(id);
        TripTransformer tt = new TripTransformer(ti);
        Document kml = tt.getKML();
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = tf.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        try {
            transformer.transform(new DOMSource(kml), result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return writer.toString();
    }
}