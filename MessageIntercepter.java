public class MessageIntercepter {
    private static String fakeData = "Fake_Data";
    public static String tamperwithMessage(String originalMessage) {
        return originalMessage + fakeData;
    }
}

