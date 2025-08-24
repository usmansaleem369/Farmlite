package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.core.model.ModelIdentifier;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Device type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Devices", type = Model.Type.USER, version = 1)
@Index(name = "undefined", fields = {"id"})
public final class Device implements Model {
  public static final QueryField ID = field("Device", "id");
  public static final QueryField DEVICE_NAME = field("Device", "DeviceName");
  public static final QueryField EMAIL = field("Device", "Email");
  public static final QueryField MAC_ADDRESS = field("Device", "MacAddress");
  public static final QueryField STATUS = field("Device", "Status");
  public static final QueryField CREATED_AT = field("Device", "createdAt");
  public static final QueryField UPDATED_AT = field("Device", "updatedAt");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String") String DeviceName;
  private final @ModelField(targetType="String") String Email;
  private final @ModelField(targetType="String") String MacAddress;
  private final @ModelField(targetType="String") String Status;
  private final @ModelField(targetType="AWSDateTime") Temporal.DateTime createdAt;
  private final @ModelField(targetType="AWSDateTime") Temporal.DateTime updatedAt;
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
  
  public String getEmail() {
      return Email;
  }
  
  public String getMacAddress() {
      return MacAddress;
  }
  
  public String getStatus() {
      return Status;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Device(String id, String DeviceName, String Email, String MacAddress, String Status, Temporal.DateTime createdAt, Temporal.DateTime updatedAt) {
    this.id = id;
    this.DeviceName = DeviceName;
    this.Email = Email;
    this.MacAddress = MacAddress;
    this.Status = Status;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Device device = (Device) obj;
      return ObjectsCompat.equals(getId(), device.getId()) &&
              ObjectsCompat.equals(getDeviceName(), device.getDeviceName()) &&
              ObjectsCompat.equals(getEmail(), device.getEmail()) &&
              ObjectsCompat.equals(getMacAddress(), device.getMacAddress()) &&
              ObjectsCompat.equals(getStatus(), device.getStatus()) &&
              ObjectsCompat.equals(getCreatedAt(), device.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), device.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getDeviceName())
      .append(getEmail())
      .append(getMacAddress())
      .append(getStatus())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Device {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("DeviceName=" + String.valueOf(getDeviceName()) + ", ")
      .append("Email=" + String.valueOf(getEmail()) + ", ")
      .append("MacAddress=" + String.valueOf(getMacAddress()) + ", ")
      .append("Status=" + String.valueOf(getStatus()) + ", ")
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
  public static Device justId(String id) {
    return new Device(
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
      DeviceName,
      Email,
      MacAddress,
      Status,
      createdAt,
      updatedAt);
  }
  public interface BuildStep {
    Device build();
    BuildStep id(String id);
    BuildStep deviceName(String deviceName);
    BuildStep email(String email);
    BuildStep macAddress(String macAddress);
    BuildStep status(String status);
    BuildStep createdAt(Temporal.DateTime createdAt);
    BuildStep updatedAt(Temporal.DateTime updatedAt);
  }
  

  public static class Builder implements BuildStep {
    private String id;
    private String DeviceName;
    private String Email;
    private String MacAddress;
    private String Status;
    private Temporal.DateTime createdAt;
    private Temporal.DateTime updatedAt;
    public Builder() {
      
    }
    
    private Builder(String id, String DeviceName, String Email, String MacAddress, String Status, Temporal.DateTime createdAt, Temporal.DateTime updatedAt) {
      this.id = id;
      this.DeviceName = DeviceName;
      this.Email = Email;
      this.MacAddress = MacAddress;
      this.Status = Status;
      this.createdAt = createdAt;
      this.updatedAt = updatedAt;
    }
    
    @Override
     public Device build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Device(
          id,
          DeviceName,
          Email,
          MacAddress,
          Status,
          createdAt,
          updatedAt);
    }
    
    @Override
     public BuildStep deviceName(String deviceName) {
        this.DeviceName = deviceName;
        return this;
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
     public BuildStep status(String status) {
        this.Status = status;
        return this;
    }
    
    @Override
     public BuildStep createdAt(Temporal.DateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    
    @Override
     public BuildStep updatedAt(Temporal.DateTime updatedAt) {
        this.updatedAt = updatedAt;
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
    private CopyOfBuilder(String id, String deviceName, String email, String macAddress, String status, Temporal.DateTime createdAt, Temporal.DateTime updatedAt) {
      super(id, DeviceName, Email, MacAddress, Status, createdAt, updatedAt);
      
    }
    
    @Override
     public CopyOfBuilder deviceName(String deviceName) {
      return (CopyOfBuilder) super.deviceName(deviceName);
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
     public CopyOfBuilder status(String status) {
      return (CopyOfBuilder) super.status(status);
    }
    
    @Override
     public CopyOfBuilder createdAt(Temporal.DateTime createdAt) {
      return (CopyOfBuilder) super.createdAt(createdAt);
    }
    
    @Override
     public CopyOfBuilder updatedAt(Temporal.DateTime updatedAt) {
      return (CopyOfBuilder) super.updatedAt(updatedAt);
    }
  }
  

  public static class DeviceIdentifier extends ModelIdentifier<Device> {
    private static final long serialVersionUID = 1L;
    public DeviceIdentifier(String id) {
      super(id);
    }
  }
  
}
