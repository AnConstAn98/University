import university.Group;
import university.University;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Group group = new Group("Economics", "http://my.nsu.ru/public/resp/student-groups/group.xvm?ref=8980");
        University university = new University("NSU", Arrays.asList(group));
        university.generateGroupInfo("C:\\Users\\ann24\\Desktop\\NSU 2018-2019\\2 сем\\прога 2\\");
    }
}
