package com.blackie.torquelogger.api;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;

import com.blackie.torquelogger.data.item.LogDetail;
import com.blackie.torquelogger.data.item.LogItem;
import com.blackie.torquelogger.data.store.LogStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.FloatingDecimal;

import javax.ws.rs.core.MediaType;

@RestController
@RequestMapping("/log")
public class LogController {
    private LogStore logStore;

    public LogController () {
        this.logStore = new LogStore();
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN)
    public ResponseEntity addLog(@RequestParam Map<String, String> pidValueMap) {

        if (isCompleteLog(pidValueMap))
            saveMapToDatabase(pidValueMap);

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    private void saveMapToDatabase (Map<String, String> pidValueMap) {
        LogItem log = getLogItemfromRequest(pidValueMap);
        logStore.save(log);
    }

    private boolean isCompleteLog (Map <String, String> pidValueMap) {
        return  pidValueMap != null &&
                //Check for all the stuff we're going to parse from the map to specific data types
                //This will prevent RTEs which slow down the server and harsh vibes
                pidValueMap.containsKey(LogDetail.SESSION) &&
                pidValueMap.containsKey(LogDetail.TIMESTAMP) &&
                pidValueMap.containsKey(LogDetail.GPS_LAT) &&
                pidValueMap.containsKey(LogDetail.GPS_LONG) &&
                pidValueMap.containsKey(LogDetail.GPS_ACC);
    }

    private LogItem getLogItemfromRequest (Map<String, String> pidValueMap) {
        LogItem log = buildLogItem(pidValueMap);
        ArrayList<LogDetail> details = new ArrayList<>();

        for (Map.Entry<String, String> entry : pidValueMap.entrySet()) {
            details.add(new LogDetail(entry.getKey(), entry.getValue()));
        }

        log.setDetails(details);

        return log;
    }

    private LogItem buildLogItem (Map<String, String> pidValueMap) {
        LogItem log = new LogItem();
        pidValueMap.remove("eml");
        pidValueMap.remove("v");
        pidValueMap.remove("id");
        //Remove these special keys so that we don't iterate over them later when we're adding the generic log details
        log.setSession(pidValueMap.remove(LogDetail.SESSION));
        //Time is coming back the same so this is useless
        log.setTs(new Timestamp(
                Long.parseLong(pidValueMap.remove(LogDetail.TIMESTAMP))
        ));
        log.setLat(FloatingDecimal.parseDouble(pidValueMap.remove(LogDetail.GPS_LAT)));
        log.setLongitude(FloatingDecimal.parseDouble(pidValueMap.remove(LogDetail.GPS_LONG)));
        log.setAccuracy(Double.parseDouble(pidValueMap.remove(LogDetail.GPS_ACC)));
        return log;
    }
}