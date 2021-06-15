package src;
public class PenJava {
    private Builder builder;

    private String color = "white";
    private Float width = 1.0f;
    private Boolean round = false;





    public PenJava(Builder builder) {
        this.builder = builder;
        color = builder.color;
    }

    public void write() {
        System.out.println("color:" + builder.color + ",with:" + builder.width + ",round:" + builder.color);
    }

    public static class Builder {
        private String color = "white";
        private Float width = 1.0f;
        private Boolean round = false;

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder width(Float width) {
            this.width = width;
            return this;
        }

        public Builder round(Boolean round) {
            this.round = round;
            return this;
        }

        public PenJava build() {
            return new PenJava(this);
        }
    }

}


