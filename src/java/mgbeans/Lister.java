/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgbeans;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.hibernate.Query;
import org.hibernate.Session;
import pojos.Contact;
import pojos.Phone;

/**
 *
 * @author Somogyi László <proceed step by step>
 */
@ManagedBean
@SessionScoped
public class Lister {

    private String name;
    private Phone selectedPhone;
    private Contact selectedContact;
    private List<Contact> contacts;
    private List<Phone> phones;

    public Lister() {
        Session session = hibernate.HibernateUtil.getSessionFactory().openSession();
        contacts = session.createQuery("From Contact").list();
        session.close();
    }

    public void listPhones(Contact c) {
        selectedContact = c;
        phones = new ArrayList<>(selectedContact.getPhones());
    }

    public void searchContact() {
        Session session = hibernate.HibernateUtil.getSessionFactory().openSession();
        Query q = session.createQuery("From Contact WHERE name LIKE :pname");
        q.setString("pname", "%" + name + "%");
        contacts = q.list();
        session.close();
    }

    public String editContact(Contact c) {
        selectedContact = c;
        return "edit_contact";
    }

    public String newContact() {
        Contact newCont = new Contact();
        selectedContact = newCont;
        return "edit_contact";
    }

    public String saveContact() {
        boolean newCont = selectedContact.getId() == null;
        Session session = hibernate.HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.saveOrUpdate(selectedContact);
        session.getTransaction().commit();
        session.close();

        if (newCont) {
            contacts.add(selectedContact);
        }
        return "index";
    }

    public String newPhone() {
        Phone newPhone = new Phone();
        selectedPhone = newPhone;
        selectedPhone.setContact(selectedContact);
        return "edit_phone";
    }

    public String editPhone(Phone pho) {
        selectedPhone = pho;
        return "edit_phone";
    }

    public String savePhone() {
        boolean newPhone = selectedPhone.getId() == null;
        Session session = hibernate.HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.saveOrUpdate(selectedPhone);
        session.getTransaction().commit();
        session.close();

        if (newPhone) {
            phones.add(selectedPhone);
            selectedContact.getPhones().add(selectedPhone);
        }
        return "index";
    }

    public void deletePhone(Phone p) {
        selectedPhone = p;
        Session session = hibernate.HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(p);
        session.getTransaction().commit();
        session.close();

        phones.remove(p);
        selectedContact.getPhones().remove(p);
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public Contact getSelectedContact() {
        return selectedContact;
    }

    public void setSelectedContact(Contact selectedContact) {
        this.selectedContact = selectedContact;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Phone getSelectedPhone() {
        return selectedPhone;
    }

    public void setSelectedPhone(Phone selectedPhone) {
        this.selectedPhone = selectedPhone;
    }

}
