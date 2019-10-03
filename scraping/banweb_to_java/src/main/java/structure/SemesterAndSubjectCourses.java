package main.java.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Contains all courses offered for a given subject + semester.
 */
public class SemesterAndSubjectCourses {
	/** The semester of classes */
	Semester semester = null;
	/** The subject of classes */
	Subject subject = null;
	/** The classes matching the semester + subject */
	List<Clazz> classes = new ArrayList<>();

	public SemesterAndSubjectCourses(Semester semester, Subject subject) {
		this.semester = semester;
		this.subject = subject;
	}

	/**
	 * @param clazz
	 *            The {@link Clazz} to remeber for this subject + semester
	 *            instance.
	 */
	public void addClass(Clazz clazz) {
		Map<String, Object> attributeValues = clazz.getAttributeValues();
		//We know a class "doesn't exist" if it doesn't have a CRN, so therefore just add this info to the last
		//class scraped. This is so that when departments do something screwy like intimately attach their recitations
		//to a lecture (lookin at you, physics), we can handle the extra day, time, location, and (possibly) instructor.
		if (!attributeValues.containsKey(Clazz.ClassAttribute.CourseReferenceNumber.shortName)) {
			Clazz prevClazz = classes.get(classes.size() - 1);
			prevClazz.addAttribute(Clazz.ClassAttribute.Days, attributeValues.get(Clazz.ClassAttribute.Days.shortName));
			prevClazz.addAttribute(Clazz.ClassAttribute.Time, attributeValues.get(Clazz.ClassAttribute.Time.shortName));
			prevClazz.addAttribute(Clazz.ClassAttribute.Location, attributeValues.get(Clazz.ClassAttribute.Location.shortName));
			prevClazz.addAttribute(Clazz.ClassAttribute.Instructor, attributeValues.get(Clazz.ClassAttribute.Instructor.shortName));
		} else {
			classes.add(clazz);
		}
	}

	/**
	 * @return The classes registered in this instance.
	 */
	public List<Clazz> getClasses() {
		return classes;
	}
	
	/**
	 * @return The semester registered for this instance.
	 */
	public Semester getSemester()
	{
		return semester;
	}
	
	/**
	 * @return The subject registered for this instance.
	 */
	public Subject getSubject()
	{
		return subject;
	}
}
