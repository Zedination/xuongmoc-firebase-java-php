package com.example.api;

import com.example.entity.DeviceTokens;
import com.google.firebase.messaging.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Transactional
@RestController
public class RestBase {
    @PersistenceContext
    private EntityManager entityManager;
    @GetMapping("/test")
    public List<DeviceTokens> testFunction(){
        List<DeviceTokens> list =  entityManager.createQuery("select t from "+DeviceTokens.class.getName()+" t", DeviceTokens.class).getResultList();
        return list;
    }
    @PostMapping("/send-notification")
    public String sendNotification(@RequestBody Map<String,String> payload) throws FirebaseMessagingException{
        String body = payload.get("content");
        String image = payload.get("image");
        String url = payload.get("url");
        System.out.println(url);
        List<String> listToken =  entityManager.createQuery("select t.token from "+DeviceTokens.class.getName()+" t", String.class).getResultList();
        System.out.println(listToken.size());
        MulticastMessage message = MulticastMessage.builder()
                /*.setNotification(Notification.builder().setTitle("Có sản phẩm mới nè, hãy vào đây xem ngay đi!")
                .setBody(body).setImage(image).build())*/
                .setWebpushConfig(WebpushConfig.builder().setFcmOptions(WebpushFcmOptions.builder().setLink(url).build())
                        /*.setNotification(WebpushNotification.builder().setTitle("Có sản phẩm mới nè, hãy vào đây xem ngay đi!")
                                .setBody(body).setImage(image).build())*/.build())
                .putData("title","Có sản phẩm mới nè, hãy vào đây xem ngay đi!")
                .putData("image",image)
                .putData("content",body).putData("url",url).addAllTokens(listToken).build();
        BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
        return response.getSuccessCount() + " thông báo đã gửi thành công!";
    }
}
