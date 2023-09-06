package sstiscanner.utils;

public class Command {
    private final String nslookup = "nslookup -q=cname <COLLABORATOR>";
    private final String curl = "curl <COLLABORATOR>";
    private String custom = "<COLLABORATOR>";
    private String selectedOption = "nslookup";

    public void setSelectedOption(String option) {
        if (option.equals("nslookup") || option.equals("curl") || option.equals("custom")) {
            this.selectedOption = option;
        } else {
            throw new IllegalArgumentException("Invalid option: " + option);
        }
    }

    public String getSelectedCommand() {
        return switch (this.selectedOption) {
            case "nslookup" -> this.nslookup;
            case "curl" -> this.curl;
            case "custom" -> this.custom;
            default -> throw new IllegalStateException("Invalid option selected.");
        };
    }

    public void setCustomCommand(String command) {
        this.custom = command;
    }
}
