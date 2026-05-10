package com.pfe.dto;

public class AssistantRequest {
    private String message;
    private String image;
    private String mimeType;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
}
