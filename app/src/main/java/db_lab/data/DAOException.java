package db_lab.data;

// This is a runtime exception we define to wrap all the exceptions coming from
// the DAO objects we're going to define.
//
// This way we won't have `SQLException`s bubbling up in all other functions.
//
public final class DAOException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DAOException(String message) {
        super(message);
    }

    public DAOException(Throwable cause) {
        super(cause);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
