package com.thach.demo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App {
    static Session session;
    static SessionFactory sessionFactory;

    private static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");

        configuration.addAnnotatedClass(Student.class);

        ServiceRegistry serviceRegistryObj = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();

        // Creating Hibernate SessionFactory Instance
        sessionFactory = configuration.buildSessionFactory(serviceRegistryObj);
        return sessionFactory;
    }

    public static void createStudent() {
        try {
            session = buildSessionFactory().openSession();
            session.beginTransaction();

            Student student = new Student();
            student.setName("Pham Van Thach");
            student.setAddress("Ho Chi Minh City, Vietnam");
            session.save(student);

            // Committing The Transactions To The Database
            session.getTransaction().commit();
            System.out.println("Successfully created Student");
        } catch(Exception sqlException) {
            if(null != session.getTransaction()) {
                System.out.println("Transaction is being rolled back");
                session.getTransaction().rollback();
            }
            sqlException.printStackTrace();
        } finally {
            if(session != null) {
                session.close();
            }
        }
    }

    public static List<Student> getStudents() {
        List<Student> students = new ArrayList<Student>();
        try {
            session = buildSessionFactory().openSession();
            session.beginTransaction();

            students = session.createQuery("FROM Student").list();
        } catch(Exception sqlException) {
            if(null != session.getTransaction()) {
                System.out.println("Transaction is being rolled back");
                session.getTransaction().rollback();
            }
            sqlException.printStackTrace();
        } finally {
            if(session != null) {
                session.close();
            }
        }
        return students;
    }

    public static void updateStudent(int id) {
        try {
            session = buildSessionFactory().openSession();
            session.beginTransaction();

            Student student = (Student) session.get(Student.class, id);
            student.setName("Jonathan Pham");
            student.setAddress("Manila, Philippines");

            // Committing The Transactions To The Database
            session.getTransaction().commit();
            System.out.println("Student with id " + id + " is successfully updated");
        } catch(Exception sqlException) {
            if(null != session.getTransaction()) {
                System.out.println("Transaction is being rolled back");
                session.getTransaction().rollback();
            }
            sqlException.printStackTrace();
        } finally {
            if(session != null) {
                session.close();
            }
        }
    }


    public static void main( String[] args ) {
        // create Student
        createStudent();

        // get Students
        List<Student> students = getStudents();
        if(students.size() > 0) {
            for(Student studentObj : students) {
                System.out.println(studentObj.toString());
            }
        } else {
            System.out.println("There's no rows in Student table");
        }

        // update student
        updateStudent(2);
    }
}
