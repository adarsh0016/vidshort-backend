package com.aivideo.storage.dto;

public class StorageStatsResponse {
    private long totalBytesUsed;
    private double totalMegabytesUsed;
    private double totalGigabytesUsed;
    private double freeTierLimitGigabytes;
    private double percentageUsed;
    private long totalVideosCount;
    private long totalImagesCount;

    public StorageStatsResponse() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private long totalBytesUsed;
        private double totalMegabytesUsed;
        private double totalGigabytesUsed;
        private double freeTierLimitGigabytes;
        private double percentageUsed;
        private long totalVideosCount;
        private long totalImagesCount;

        public Builder totalBytesUsed(long totalBytesUsed) { this.totalBytesUsed = totalBytesUsed; return this; }
        public Builder totalMegabytesUsed(double totalMegabytesUsed) { this.totalMegabytesUsed = totalMegabytesUsed; return this; }
        public Builder totalGigabytesUsed(double totalGigabytesUsed) { this.totalGigabytesUsed = totalGigabytesUsed; return this; }
        public Builder freeTierLimitGigabytes(double freeTierLimitGigabytes) { this.freeTierLimitGigabytes = freeTierLimitGigabytes; return this; }
        public Builder percentageUsed(double percentageUsed) { this.percentageUsed = percentageUsed; return this; }
        public Builder totalVideosCount(long totalVideosCount) { this.totalVideosCount = totalVideosCount; return this; }
        public Builder totalImagesCount(long totalImagesCount) { this.totalImagesCount = totalImagesCount; return this; }

        public StorageStatsResponse build() {
            StorageStatsResponse r = new StorageStatsResponse();
            r.totalBytesUsed = totalBytesUsed;
            r.totalMegabytesUsed = totalMegabytesUsed;
            r.totalGigabytesUsed = totalGigabytesUsed;
            r.freeTierLimitGigabytes = freeTierLimitGigabytes;
            r.percentageUsed = percentageUsed;
            r.totalVideosCount = totalVideosCount;
            r.totalImagesCount = totalImagesCount;
            return r;
        }
    }

    public long getTotalBytesUsed() { return totalBytesUsed; }
    public void setTotalBytesUsed(long totalBytesUsed) { this.totalBytesUsed = totalBytesUsed; }
    public double getTotalMegabytesUsed() { return totalMegabytesUsed; }
    public void setTotalMegabytesUsed(double totalMegabytesUsed) { this.totalMegabytesUsed = totalMegabytesUsed; }
    public double getTotalGigabytesUsed() { return totalGigabytesUsed; }
    public void setTotalGigabytesUsed(double totalGigabytesUsed) { this.totalGigabytesUsed = totalGigabytesUsed; }
    public double getFreeTierLimitGigabytes() { return freeTierLimitGigabytes; }
    public void setFreeTierLimitGigabytes(double freeTierLimitGigabytes) { this.freeTierLimitGigabytes = freeTierLimitGigabytes; }
    public double getPercentageUsed() { return percentageUsed; }
    public void setPercentageUsed(double percentageUsed) { this.percentageUsed = percentageUsed; }
    public long getTotalVideosCount() { return totalVideosCount; }
    public void setTotalVideosCount(long totalVideosCount) { this.totalVideosCount = totalVideosCount; }
    public long getTotalImagesCount() { return totalImagesCount; }
    public void setTotalImagesCount(long totalImagesCount) { this.totalImagesCount = totalImagesCount; }
}
