import java.awt.Color;

import com.fasterxml.jackson.annotation.*;

public class Texture {
  private String name;
  private int r;
  private int g;
  private int b;

  @JsonCreator
  public Texture(@JsonProperty("name") String name, @JsonProperty("r") int r, @JsonProperty("g") int g,
      @JsonProperty("b") int b) {
    this.name = name;
    this.r = r;
    this.g = g;
    this.b = b;
  }

  public int getR() {
    return this.r;
  }

  public int getG() {
    return this.g;
  }

  public int getB() {
    return this.b;
  }

  public String getName() {
    return this.name;
  }

  public Color getColor() {
    return new Color(this.r, this.g, this.b);
  }

  public int getDistance(Color current, int min) {
    return Math.abs(this.r - current.getRed()) +
        Math.abs(this.g - current.getGreen()) +
        Math.abs(this.b - current.getBlue());
  }

  @Override
  public String toString() {
    return String.format("Name: %s, Color: %s", this.name, this.getColor());
  }
}
