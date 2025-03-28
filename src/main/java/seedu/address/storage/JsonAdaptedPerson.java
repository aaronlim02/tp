package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.leave.Leave;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Hire;
import seedu.address.model.person.Name;
import seedu.address.model.person.Nric;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Tag;
import seedu.address.model.person.TagSet;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String nric;
    private final String phone;
    private final String email;
    private final String address;
    private final String hire;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final List<JsonAdaptedLeave> leaves = new ArrayList<>();
    private final JsonAdaptedAttendance attendance;

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("nric") String nric,
            @JsonProperty("phone") String phone,
            @JsonProperty("email") String email, @JsonProperty("address") String address,
            @JsonProperty("hire") String hire, @JsonProperty("tags") List<JsonAdaptedTag> tags,
            @JsonProperty("leaves") List<JsonAdaptedLeave> leaves,
            @JsonProperty("attendance") JsonAdaptedAttendance attendance) {

        this.name = name;
        this.nric = nric;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.hire = hire;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        if (leaves != null) {
            this.leaves.addAll(leaves);
        }
        this.attendance = attendance;
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        nric = source.getNric().nric;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        hire = source.getHire().hire;
        tags.addAll(source.getTags().getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList()));
        leaves.addAll(source.getLeaves().stream()
            .map(JsonAdaptedLeave::new)
            .collect(Collectors.toList()));
        attendance = new JsonAdaptedAttendance(source.getAttendance());
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final Set<Tag> personTags = new HashSet<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        final TagSet modelTags = new TagSet(personTags);

        final List<Leave> personLeaves = new ArrayList<>();
        for (JsonAdaptedLeave leave : leaves) {
            personLeaves.add(leave.toModelType());
        }

        final Attendance modelAttendance;
        if (attendance == null) {
            modelAttendance = new Attendance(); // default if missing
        } else {
            modelAttendance = attendance.toModelType(); // deserialize
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (nric == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Nric.class.getSimpleName()));
        }
        if (!Nric.isValidNric(nric)) {
            throw new IllegalValueException(Nric.MESSAGE_CONSTRAINTS);
        }
        final Nric modelNric = new Nric(nric);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        if (hire == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Hire.class.getSimpleName()));
        }
        if (!Hire.isValidHire(hire)) {
            throw new IllegalValueException(Hire.MESSAGE_CONSTRAINTS);
        }
        final Hire modelHire = new Hire(hire);

        return new Person(modelName, modelNric, modelPhone, modelEmail,
                modelAddress, modelHire, modelTags, personLeaves, modelAttendance);
    }
}
