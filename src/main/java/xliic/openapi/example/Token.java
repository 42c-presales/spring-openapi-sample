package xliic.openapi.example;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Token {
     @NotNull
     @Size (min = 36, max = 36)
     private String tokenID;
     @NotNull

     @Size (min = 15, max = 1000)
     private String value;

     public String getTokenID() {
          return tokenID;
     }

     public void setTokenID(String tokenID) {
          this.tokenID = tokenID;
     }

     public String getValue() {
          return value;
     }

     public void setValue(String value) {
          this.value = value;
     }
}
