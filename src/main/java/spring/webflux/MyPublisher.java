package spring.webflux;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;

public class MyPublisher implements Flow.Publisher<Message> {
    private final List<Flow.Subscriber<? super Message>> subscribers = new ArrayList<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    @Override
    public void subscribe(Flow.Subscriber<? super Message> subscriber) {
        System.out.println("Called subscribe() in MyPublisher.");
        subscribers.add(subscriber);
        subscriber.onSubscribe(new MySubscription(this));
    }

    public void notifySubscribers(Message message) {
        System.out.println("Called notifySubscribers() in MyPublisher.");
        for(Flow.Subscriber<? super Message> subscriber : subscribers) {
            executor.submit(() -> subscriber.onNext(message));
        }
    }

    public void close() {
        System.out.println("Called close() in MyPublisher.");
        for(Flow.Subscriber<? super Message> subscriber : subscribers) {
            subscriber.onComplete();
        }
        executor.shutdown();
    }
}