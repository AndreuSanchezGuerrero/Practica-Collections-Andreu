public class ExcepcionsPropies extends Exception {

    public static class LimitProductesException extends Exception {
        public LimitProductesException(String message) {
            super(message);
        }
    }

    public static class DataCaducitatException extends Exception {
        public DataCaducitatException(String message) {
            super(message);
        }
    }

    public static class negatiuException extends Exception {
        public negatiuException(String message) {
            super(message);
        }
    }

    public static class LimitCaracteresException extends Exception {
        public LimitCaracteresException(String message) {
            super(message);
        }
    }

    public static class enumFailException extends Exception {
        public enumFailException(String message) {
            super(message);
        }
    }
}
