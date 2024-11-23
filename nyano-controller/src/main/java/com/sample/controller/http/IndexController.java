package com.sample.controller.http;

import com.sample.application.service.event.EventAppService;
import com.sample.application.service.event.EventAppServiceRedis;
import com.sample.application.service.ticket.TicketDetailAppService;
import com.sample.domain.model.HiDomainEntity;
import com.sample.domain.model.TicketDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/index")
public class IndexController {
    @Autowired
    private EventAppService eventAppService;
    @Autowired
    private EventAppServiceRedis eventAppServiceRedis;
    @Autowired
    private TicketDetailAppService ticketDetailAppService;


    @GetMapping("")
    public String index() {
        String key = "hainh";
        String data = "";
        data = eventAppServiceRedis.getSayHiRedis(key);
        if (!(data == null)) {
            return data;
        }
        String datas = eventAppService.SayHi("hainh");
        System.out.println("data::" + datas);
        eventAppServiceRedis.saveSayHi("chain");
        return datas;
    }

    @GetMapping("/get-all")
    public List<HiDomainEntity> getAll() {
        return eventAppService.getAllHiEntities();
    }


    @GetMapping(value = "/ticket/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public TicketDetail getTicket(@PathVariable("id") Long id) {
        return ticketDetailAppService.getTicketDetailById(id);
    }
}
