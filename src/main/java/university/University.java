package university;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class University {
    private String name;
    private Collection<Group> groups;

    public University(String name, Collection<Group> groups) {
        this.name = name;
        this.groups = groups;
    }

    public void generateGroupInfo(String pathToFolder) {
        groups.forEach(group -> {
            try {
                List<Student> students = downloadStudents(group);
                writeToFile(pathToFolder, group.getName(), students);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void writeToFile(String pathToFolder, String name, List<Student> students) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(pathToFolder + name);
        students.forEach(student -> {
            final String string;
            if (student.getStatus() != null && !student.getStatus().isEmpty()) {
                string = String.join(",", student.getLastName(), student.getFirstName(), student.getMiddleName(), student.getStatus());
            } else {
                string = String.join(",", student.getLastName(), student.getFirstName(), student.getMiddleName());
            }
            writer.println(string);
        });
        writer.flush();
    }

    private List<Student> downloadStudents(Group group) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new URL(group.getUrl()).openStream());

        List<Student> result = new ArrayList<>();

        final NodeList groupChild = doc.getChildNodes();
        final Node node = groupChild.item(0);
        final NodeList studentNodes = node.getChildNodes();
        for (int i = 0; i < studentNodes.getLength(); i++) {
            final Node childNode = studentNodes.item(i);
            if (childNode.getNodeName().equals("student")) {
                final Node nameNode = childNode.getAttributes().getNamedItem("name");
                final Node statusNode = childNode.getAttributes().getNamedItem("status");
                final String[] names = nameNode.getNodeValue().split(" ");

                final String lastName = names[0];
                final String firstName = names[1];
                final String middleName = names[2];
                final String status = statusNode.getNodeValue();
                result.add(new Student(firstName, middleName, lastName, status));
            }
        }
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Group> getGroups() {
        return groups;
    }

    public void setGroups(Collection<Group> groups) {
        this.groups = groups;
    }
}
