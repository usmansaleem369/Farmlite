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

/** This is an auto generated class representing the DevicesTable type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "DevicesTables", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class DevicesTable implements Model {
  public static final QueryField ID = field("DevicesTable", "id");
  public static final QueryField DEVICE_NAME = field("DevicesTable", "DeviceName");
  public static final QueryField MAC_ADDRESS = field("DevicesTable", "MacAddress");
  public static final QueryField STATUS = field("DevicesTable", "Status");
  public static final QueryField EMAIL = field("DevicesTable", "Email");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String") String DeviceName;
  private final @ModelField(targetType="String") String MacAddress;
  private final @ModelField(targetType="String") String Status;
  private final @ModelField(targetType="String") String Email;
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
  
  public String getDeviceName() {
      return DeviceName;
  }
  
  public String getMacAddress() {
      return MacAddress;
  }
  
  public String getStatus() {
      return Status;
  }
  
  public String getEmail() {
      return Email;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private DevicesTable(String id, String DeviceName, String MacAddress, String Status, String Email) {
    this.id = id;
    this.DeviceName = DeviceName;
    this.MacAddress = MacAddress;
    this.Status = Status;
    this.Email = Email;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      DevicesTable devicesTable = (DevicesTable) obj;
      return ObjectsCompat.equals(getId(), devicesTable.getId()) &&
              ObjectsCompat.equals(getDeviceName(), devicesTable.getDeviceName()) &&
              ObjectsCompat.equals(getMacAddress(), devicesTable.getMacAddress()) &&
              ObjectsCompat.equals(getStatus(), devicesTable.getStatus()) &&
              ObjectsCompat.equals(getEmail(), devicesTable.getEmail()) &&
              ObjectsCompat.equals(getCreatedAt(), devicesTable.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), devicesTable.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getDeviceName())
      .append(getMacAddress())
      .append(getStatus())
      .append(getEmail())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("DevicesTable {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("DeviceName=" + String.valueOf(getDeviceName()) + ", ")
      .append("MacAddress=" + String.valueOf(getMacAddress()) + ", ")
      .append("Status=" + String.valueOf(getStatus()) + ", ")
      .append("Email=" + String.valueOf(getEmail()) + ", ")
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
  public static DevicesTable justId(String id) {
    return new DevicesTable(
      id,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      DeviceName,
      MacAddress,
      Status,
      Email);
  }
  public interface BuildStep {
    DevicesTable build();
    BuildStep id(String id);
    BuildStep deviceName(String deviceName);
    BuildStep macAddress(String macAddress);
    BuildStep status(String status);
    BuildStep email(String email);
  }
  

  public static class Builder implements BuildStep {
    private String id;
    private String DeviceName;
    private String MacAddress;
    private String Status;
    private String Email;
    public Builder() {
      
    }
    
    private Builder(String id, String DeviceName, String MacAddress, String Status, String Email) {
      this.id = id;
      this.DeviceName = DeviceName;
      this.MacAddress = MacAddress;
      this.Status = Status;
      this.Email = Email;
    }
    
    @Override
     public DevicesTable build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new DevicesTable(
          id,
          DeviceName,
          MacAddress,
          Status,
          Email);
    }
    
    @Override
     public BuildStep deviceName(String deviceName) {
        this.DeviceName = deviceName;
        return this;
    }
    
    @Override
     public BuildStep macAddress(String macAddress) {
        this.MacAddress = macAddress;
        return this;
    }
    
    @Override
     public BuildStep status(String status) {
        this.Status = status;
        return this;
    }
    
    @Override
     public BuildStep email(String email) {
        this.Email = email;
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
    private CopyOfBuilder(String id, String deviceName, String macAddress, String status, String email) {
      super(id, DeviceName, MacAddress, Status, Email);
      
    }
    
    @Override
     public CopyOfBuilder deviceName(String deviceName) {
      return (CopyOfBuilder) super.deviceName(deviceName);
    }
    
    @Override
     public CopyOfBuilder macAddress(String macAddress) {
      return (CopyOfBuilder) super.macAddress(macAddress);
    }
    
    @Override
     public CopyOfBuilder status(String status) {
      return (CopyOfBuilder) super.status(status);
    }
    
    @Override
     public CopyOfBuilder email(String email) {
      return (CopyOfBuilder) super.email(email);
    }
  }
  

  public static class DevicesTableIdentifier extends ModelIdentifier<DevicesTable> {
    private static final long serialVersionUID = 1L;
    public DevicesTableIdentifier(String id) {
      super(id);
    }
  }
  
}
