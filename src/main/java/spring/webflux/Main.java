package spring.webflux;

public class Main {
    public static void main(String[] args) throws InterruptedException{
        MyPublisher publisher = new MyPublisher();
        MySubscriber subscriber = new MySubscriber();

        publisher.subscribe(subscriber);
        publisher.notifySubscribers(new Message(0, "Initial Message"));

        Thread.sleep(200);

        publisher.close();
    }
}