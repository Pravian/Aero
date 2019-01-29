package net.pravian.aero.command.dynamic;

public class ExecutionException extends RuntimeException {

  public ExecutionException(Exception ex) {
    super(ex instanceof RuntimeException ? ex : new RuntimeException(ex));
  }

  @Override
  public RuntimeException getCause() {
    return (RuntimeException) super.getCause();
  }
}
