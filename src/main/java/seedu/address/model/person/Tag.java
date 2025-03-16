package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Tag assigned to a Person in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidTagName(String)}
 */
public class Tag {

    public static final String MESSAGE_CONSTRAINTS = "Tags names should be alphanumeric, " +
                                                    "non-empty, and can include spaces and punctuation like .,'-";

    /*
     * The tag name should contain only alphanumeric characters, spaces, and punctuation like .,'- and must not be blank.
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum} .,'-]+";

    public final String tagName;

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name.
     */
    public Tag(String tagName) {
        requireNonNull(tagName);
        checkArgument(isValidTagName(tagName), MESSAGE_CONSTRAINTS);
        this.tagName = tagName;
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return tagName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Tag)) {
            return false;
        }

        Tag otherTag = (Tag) other;
        return tagName.equals(otherTag.tagName);
    }

    @Override
    public int hashCode() {
        return tagName.hashCode();
    }
}
