package spring.webflux;

import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicInteger;

public class MySubscriber implements Flow.Subscriber<Message> {
    private Flow.Subscription subscription;
    private final int bufferSize = 10;
    private final AtomicInteger processedItems = new AtomicInteger(0);  // 동시성 프로그래밍을 위한 객체

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        System.out.println("Called onSubscribe() in MySubscriber.");
        this.subscription = subscription;
        subscription.request(bufferSize);   // 처음에 몇 개의 아이템을 받을 것인지 요청
    }

    @Override
    public void onNext(Message item) {
        System.out.println("Called onNext() in MySubscriber. Received: " + item + ", ProcessedItems: " + processedItems.incrementAndGet());
        if(processedItems.get() >= bufferSize) {
            System.out.println("Buffer is full.");
            processedItems.set(0);
            subscription.request(2);    // 버퍼가 다 차면 다시 요청
        }
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("Called onError() in MySubscriber.");
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("Called onComplete() in MySubscriber.");
        System.out.println("All items received.");
    }
}
