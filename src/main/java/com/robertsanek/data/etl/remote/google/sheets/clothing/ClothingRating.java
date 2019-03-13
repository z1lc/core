package com.robertsanek.data.etl.remote.google.sheets.clothing;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "clothing_ratings")
public class ClothingRating {

  static final int MAX_LENGTH = 10_000;

  @Id
  private Long id;
  @Column(name = "general_class")
  private String generalClass;
  @Column(name = "sub_class")
  private String subClass;
  @Column(name = "description_of_stock", length = MAX_LENGTH)
  private String descriptionOfStock;
  @Column(name = "all_tags_removed")
  private Boolean allTagsRemoved;
  @Column(name = "tags_comment", length = MAX_LENGTH)
  private String tagsComment;
  @Column(name = "all_items_fit_correctly")
  private Boolean allItemsFitCorrectly;
  @Column(name = "fit_comment", length = MAX_LENGTH)
  private String fitComment;
  @Column(name = "all_items_great")
  private Boolean allItemsGreat;
  @Column(name = "great_comment", length = MAX_LENGTH)
  private String greatComment;
  @Column(name = "enough_total_items")
  private Boolean enoughTotalItems;
  @Column(name = "total_comment", length = MAX_LENGTH)
  private String totalComment;

  public static final class ClothingRatingBuilder {

    private Long id;
    private String generalClass;
    private String subClass;
    private String descriptionOfStock;
    private Boolean allTagsRemoved;
    private String tagsComment;
    private Boolean allItemsFitCorrectly;
    private String fitComment;
    private Boolean allItemsGreat;
    private String greatComment;
    private Boolean enoughTotalItems;
    private String totalComment;

    private ClothingRatingBuilder() {}

    public static ClothingRatingBuilder aClothingRating() {
      return new ClothingRatingBuilder();
    }

    public ClothingRatingBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public ClothingRatingBuilder withGeneralClass(String generalClass) {
      this.generalClass = generalClass;
      return this;
    }

    public ClothingRatingBuilder withSubClass(String subClass) {
      this.subClass = subClass;
      return this;
    }

    public ClothingRatingBuilder withDescriptionOfStock(String descriptionOfStock) {
      this.descriptionOfStock = descriptionOfStock;
      return this;
    }

    public ClothingRatingBuilder withAllTagsRemoved(Boolean allTagsRemoved) {
      this.allTagsRemoved = allTagsRemoved;
      return this;
    }

    public ClothingRatingBuilder withTagsComment(String tagsComment) {
      this.tagsComment = tagsComment;
      return this;
    }

    public ClothingRatingBuilder withAllItemsFitCorrectly(Boolean allItemsFitCorrectly) {
      this.allItemsFitCorrectly = allItemsFitCorrectly;
      return this;
    }

    public ClothingRatingBuilder withFitComment(String fitComment) {
      this.fitComment = fitComment;
      return this;
    }

    public ClothingRatingBuilder withAllItemsGreat(Boolean allItemsGreat) {
      this.allItemsGreat = allItemsGreat;
      return this;
    }

    public ClothingRatingBuilder withGreatComment(String greatComment) {
      this.greatComment = greatComment;
      return this;
    }

    public ClothingRatingBuilder withEnoughTotalItems(Boolean enoughTotalItems) {
      this.enoughTotalItems = enoughTotalItems;
      return this;
    }

    public ClothingRatingBuilder withTotalComment(String totalComment) {
      this.totalComment = totalComment;
      return this;
    }

    public ClothingRating build() {
      ClothingRating clothingRating = new ClothingRating();
      clothingRating.fitComment = this.fitComment;
      clothingRating.subClass = this.subClass;
      clothingRating.allItemsGreat = this.allItemsGreat;
      clothingRating.totalComment = this.totalComment;
      clothingRating.allTagsRemoved = this.allTagsRemoved;
      clothingRating.tagsComment = this.tagsComment;
      clothingRating.id = this.id;
      clothingRating.greatComment = this.greatComment;
      clothingRating.generalClass = this.generalClass;
      clothingRating.enoughTotalItems = this.enoughTotalItems;
      clothingRating.descriptionOfStock = this.descriptionOfStock;
      clothingRating.allItemsFitCorrectly = this.allItemsFitCorrectly;
      return clothingRating;
    }
  }
}
