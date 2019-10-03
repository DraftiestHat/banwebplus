package main.java.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Collector to hold all class data from scraped courses.
 */
public class Clazz {
	
	/**
	 * The available class attributes to scrape, plus subject.
	 */
	public static enum ClassAttribute
	{	
		CourseReferenceNumber("CRN", true, false),
		CourseShortName("Course", false, false),
		Campus("*Campus", false, false),
		Days("Days", false, true),
		Time("Time", false, true),
		Location("Location", false, true),
		Hours("Hrs", true, false),
		Title("Title", false, false),
		Instructor("Instructor", false, true),
		SeatsAvailable("Seats", true, false),
		SeatsLimit("Limit", true, false),
		EnrolledStudents("Enroll", true, false),
		Subject("subject", false, false);
		
		public String shortName = "";
		public Boolean interpretAsInteger = false;
		//this is here so that we can handle when a class has multiple locations, days, times, and instructors.
		//See SemesterAndSubjectCourses for the larger explanation.
		public Boolean allowMultiple = false;
		
		ClassAttribute(String shortName, Boolean interpretAsInteger, Boolean allowMultiple)
		{
			this.shortName = shortName;
			this.interpretAsInteger = interpretAsInteger;
			this.allowMultiple = allowMultiple;
		}
	}
	
	/** Container for the class attributes to their values. */
	protected Map<ClassAttribute, Object> attributes = new LinkedHashMap<>();

	/**
	 * Adds the given attribute to this clazz.
	 *
	 * @param attributeType The type of the attribute to add.
	 * @param value The value of the given attribute. Nulls are ignored.
	 * @throws IllegalArgumentException if an attribute is of uknown type, or the given value is not a string, integer,
	 * or a subclass of Collection
	 */
	public void addAttribute(ClassAttribute attributeType, Object value) throws IllegalArgumentException
	{
		//If we know that it's an instance of string or int, we can just add the value right on.
		if (value instanceof String || value instanceof Integer) {
			addAttributePrivate(attributeType, value);
		} else if (value instanceof Collection) {
			//we have to go through each value to add it now.
			Collection valueCollection = (Collection) value;
			for (Object val : valueCollection) {
				if (val instanceof String || val instanceof Integer) {
					addAttributePrivate(attributeType, val);
				//ignore null values
				} else if (val != null) {
					throw new IllegalArgumentException("Trying to add a new attribute, but it's of type \"" + val.getClass() + "\", not \"String\".");
				}
			}
		//ignore null values (again)
		} else if (value != null) {
			//we don't know how to handle things that aren't collections, strings, ints, or null
			throw new IllegalArgumentException("Trying to add a new attribute, but was given an unhandled type \"" + value.getClass() + "\", not \"String\" or a sub-class of \"Collection\".");
		}

	}

	private void addAttributePrivate(ClassAttribute attributeType, Object val) {
		//If we need to allow multiple attributes, always have it be a list
		if (attributeType.allowMultiple) {
			//either the list already exists, or we need to make one
			List multList = (List)attributes.getOrDefault(attributeType, new ArrayList());
			//do the regular stuff now on - cast to int if needs to be and add it  OR just add the straight value
			if (attributeType.interpretAsInteger) {
				if (val instanceof String) {
					multList.add(new Integer((String) val));
				} else if (val instanceof Integer) {
					multList.add(val);
				} else {
					throw new IllegalArgumentException("A val was not an \"Integer\" or a \"String\", but a(n) \"" + val.getClass() + "\"");
				}
			} else {
				multList.add(val);
			}
			//Make sure that the attribute type actually holds the list.
			attributes.put(attributeType, multList);
		//Now we know we need to just add the value, but it should be an integer
		} else if (attributeType.interpretAsInteger) {
			//It might be a string
			if (val instanceof String) {
				attributes.put(attributeType, new Integer((String) val));
			//or now, it might already be an int, so we don't need to do anything.
			} else if (val instanceof Integer) {
				attributes.put(attributeType, val);
			} else {
				throw new IllegalArgumentException("A val was not an \"Integer\" or a \"String\", but a(n) \"" + val.getClass() + "\"");
			}
		} else {
			attributes.put(attributeType, val);
		}
	}

	/**
	 * Creates a map containing the attributes (using their abbreviated names)
	 * to their values.
	 * 
	 * @return The newly created map of "abbreviated name" => value.
	 */
	public Map<String, Object> getAttributeValues()
	{
		Map<String, Object> retval = new TreeMap<>();
		
		for (ClassAttribute attribute : attributes.keySet())
		{
			retval.put(attribute.shortName, attributes.get(attribute));
		}
		
		return retval;
	}
}
