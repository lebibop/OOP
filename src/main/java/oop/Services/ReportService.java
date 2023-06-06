package oop.Services;


import oop.Helpers.HibernateUtil;
import oop.Model.Report;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;


/**
 * Класс для работы с таблицей Report в базе данных
 * @author lebibop
 */
public class ReportService {
    /**
     * Обновляет отчет в базе данных.
     * @param report объект, представляющий отчет
     * @throws HibernateException если возникла ошибка при работе с Hibernate
     */
    public void updateReport(Report report) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(report);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    /**

     * Возвращает список отчетов за указанный месяц.
     * @param month номер месяца (1-12)
     * @return список объектов, представляющих отчеты за указанный месяц
     * @throws HibernateException если возникла ошибка при работе с Hibernate
     */
    public List<Report> getReports(Integer month) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Report> query = builder.createQuery(Report.class);
            Root<Report> root = query.from(Report.class);
            query.select(root);
            query.where(builder.equal(root.get("month"), month));
            return session.createQuery(query).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}