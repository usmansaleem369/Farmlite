package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.core.model.ModelIdentifier;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the SchedulesModes type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "SchedulesModes", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class SchedulesModes implements Model {
  public static final QueryField ID = field("SchedulesModes", "id");
  public static final QueryField EMAIL = field("SchedulesModes", "Email");
  public static final QueryField MAC_ADDRESS = field("SchedulesModes", "MacAddress");
  public static final QueryField MODE_LT = field("SchedulesModes", "Mode_LT");
  public static final QueryField MODE_LL = field("SchedulesModes", "Mode_LL");
  public static final QueryField MODE_TL = field("SchedulesModes", "Mode_TL");
  public static final QueryField MODE_TT = field("SchedulesModes", "Mode_TT");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String") String Email;
  private final @ModelField(targetType="String") String MacAddress;
  private final @ModelField(targetType="String") String Mode_LT;
  private final @ModelField(targetType="String") String Mode_LL;
  private final @ModelField(targetType="String") String Mode_TL;
  private final @ModelField(targetType="String") String Mode_TT;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  /** @deprecated This API is internal to Amplify and should not be used. */
  @Deprecated
   public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public String getEmail() {
      return Email;
  }
  
  public String getMacAddress() {
      return MacAddress;
  }
  
  public String getModeLt() {
      return Mode_LT;
  }
  
  public String getModeLl() {
      return Mode_LL;
  }
  
  public String getModeTl() {
      return Mode_TL;
  }
  
  public String getModeTt() {
      return Mode_TT;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private SchedulesModes(String id, String Email, String MacAddress, String Mode_LT, String Mode_LL, String Mode_TL, String Mode_TT) {
    this.id = id;
    this.Email = Email;
    this.MacAddress = MacAddress;
    this.Mode_LT = Mode_LT;
    this.Mode_LL = Mode_LL;
    this.Mode_TL = Mode_TL;
    this.Mode_TT = Mode_TT;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      SchedulesModes schedulesModes = (SchedulesModes) obj;
      return ObjectsCompat.equals(getId(), schedulesModes.getId()) &&
              ObjectsCompat.equals(getEmail(), schedulesModes.getEmail()) &&
              ObjectsCompat.equals(getMacAddress(), schedulesModes.getMacAddress()) &&
              ObjectsCompat.equals(getModeLt(), schedulesModes.getModeLt()) &&
              ObjectsCompat.equals(getModeLl(), schedulesModes.getModeLl()) &&
              ObjectsCompat.equals(getModeTl(), schedulesModes.getModeTl()) &&
              ObjectsCompat.equals(getModeTt(), schedulesModes.getModeTt()) &&
              ObjectsCompat.equals(getCreatedAt(), schedulesModes.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), schedulesModes.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getEmail())
      .append(getMacAddress())
      .append(getModeLt())
      .append(getModeLl())
      .append(getModeTl())
      .append(getModeTt())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("SchedulesModes {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("Email=" + String.valueOf(getEmail()) + ", ")
      .append("MacAddress=" + String.valueOf(getMacAddress()) + ", ")
      .append("Mode_LT=" + String.valueOf(getModeLt()) + ", ")
      .append("Mode_LL=" + String.valueOf(getModeLl()) + ", ")
      .append("Mode_TL=" + String.valueOf(getModeTl()) + ", ")
      .append("Mode_TT=" + String.valueOf(getModeTt()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static BuildStep builder() {
      return new Builder();
  }
  
  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static SchedulesModes justId(String id) {
    return new SchedulesModes(
      id,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      Email,
      MacAddress,
      Mode_LT,
      Mode_LL,
      Mode_TL,
      Mode_TT);
  }
  public interface BuildStep {
    SchedulesModes build();
    BuildStep id(String id);
    BuildStep email(String email);
    BuildStep macAddress(String macAddress);
    BuildStep modeLt(String modeLt);
    BuildStep modeLl(String modeLl);
    BuildStep modeTl(String modeTl);
    BuildStep modeTt(String modeTt);
  }
  

  public static class Builder implements BuildStep {
    private String id;
    private String Email;
    private String MacAddress;
    private String Mode_LT;
    private String Mode_LL;
    private String Mode_TL;
    private String Mode_TT;
    public Builder() {
      
    }
    
    private Builder(String id, String Email, String MacAddress, String Mode_LT, String Mode_LL, String Mode_TL, String Mode_TT) {
      this.id = id;
      this.Email = Email;
      this.MacAddress = MacAddress;
      this.Mode_LT = Mode_LT;
      this.Mode_LL = Mode_LL;
      this.Mode_TL = Mode_TL;
      this.Mode_TT = Mode_TT;
    }
    
    @Override
     public SchedulesModes build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new SchedulesModes(
          id,
          Email,
          MacAddress,
          Mode_LT,
          Mode_LL,
          Mode_TL,
          Mode_TT);
    }
    
    @Override
     public BuildStep email(String email) {
        this.Email = email;
        return this;
    }
    
    @Override
     public BuildStep macAddress(String macAddress) {
        this.MacAddress = macAddress;
        return this;
    }
    
    @Override
     public BuildStep modeLt(String modeLt) {
        this.Mode_LT = modeLt;
        return this;
    }
    
    @Override
     public BuildStep modeLl(String modeLl) {
        this.Mode_LL = modeLl;
        return this;
    }
    
    @Override
     public BuildStep modeTl(String modeTl) {
        this.Mode_TL = modeTl;
        return this;
    }
    
    @Override
     public BuildStep modeTt(String modeTt) {
        this.Mode_TT = modeTt;
        return this;
    }
    
    /**
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String email, String macAddress, String modeLt, String modeLl, String modeTl, String modeTt) {
      super(id, Email, MacAddress, Mode_LT, Mode_LL, Mode_TL, Mode_TT);
      
    }
    
    @Override
     public CopyOfBuilder email(String email) {
      return (CopyOfBuilder) super.email(email);
    }
    
    @Override
     public CopyOfBuilder macAddress(String macAddress) {
      return (CopyOfBuilder) super.macAddress(macAddress);
    }
    
    @Override
     public CopyOfBuilder modeLt(String modeLt) {
      return (CopyOfBuilder) super.modeLt(modeLt);
    }
    
    @Override
     public CopyOfBuilder modeLl(String modeLl) {
      return (CopyOfBuilder) super.modeLl(modeLl);
    }
    
    @Override
     public CopyOfBuilder modeTl(String modeTl) {
      return (CopyOfBuilder) super.modeTl(modeTl);
    }
    
    @Override
     public CopyOfBuilder modeTt(String modeTt) {
      return (CopyOfBuilder) super.modeTt(modeTt);
    }
  }
  

  public static class SchedulesModesIdentifier extends ModelIdentifier<SchedulesModes> {
    private static final long serialVersionUID = 1L;
    public SchedulesModesIdentifier(String id) {
      super(id);
    }
  }
  
}
