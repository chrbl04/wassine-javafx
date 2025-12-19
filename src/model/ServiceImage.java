package model;

import java.time.LocalDateTime;

public class ServiceImage {
    private int imageId;
    private int serviceId;
    private String imageUrl;
    private String caption;
    private boolean primary;
    private int sortOrder;
    private LocalDateTime uploadedAt;

    public ServiceImage() {}

    public ServiceImage(int imageId, int serviceId, String imageUrl, String caption,
                        boolean primary, int sortOrder, LocalDateTime uploadedAt) {
        this.imageId = imageId;
        this.serviceId = serviceId;
        this.imageUrl = imageUrl;
        this.caption = caption;
        this.primary = primary;
        this.sortOrder = sortOrder;
        this.uploadedAt = uploadedAt;
    }

    public int getImageId() { return imageId; }
    public void setImageId(int imageId) { this.imageId = imageId; }

    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    public boolean isPrimary() { return primary; }
    public void setPrimary(boolean primary) { this.primary = primary; }

    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }

    @Override
    public String toString() {
        return "ServiceImage{" +
                "imageId=" + imageId +
                ", serviceId=" + serviceId +
                ", imageUrl='" + imageUrl + '\'' +
                ", primary=" + primary +
                ", sortOrder=" + sortOrder +
                ", uploadedAt=" + uploadedAt +
                '}';
    }
}
