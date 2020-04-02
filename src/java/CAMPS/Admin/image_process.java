/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CAMPS.Admin;

import CAMPS.Connect.DBConnect;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@MultipartConfig(maxFileSize = 16177215)
public class image_process extends HttpServlet {


    public BufferedImage getScaledInstance(BufferedImage img,
            int targetWidth,
            int targetHeight,
            boolean higherQuality) {
        int type = (img.getTransparency() == Transparency.OPAQUE)
                ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = (BufferedImage) img;
        int w, h;
        if (higherQuality) {
            // Use multi-step technique: start with original size, then
            // scale down in multiple passes with drawImage()
            // until the target size is reached
            w = img.getWidth();
            h = img.getHeight();
        } else {
            // Use one-step technique: scale directly from original
            // size to target size with a single drawImage() call
            w = targetWidth;
            h = targetHeight;
        }

        do {
            if (higherQuality && w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }

            if (higherQuality && h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }

            BufferedImage tmp = new BufferedImage(w, h, type);
            Graphics2D g2 = tmp.createGraphics();
            Object hint = "";
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose(); 

            ret = tmp;
        } while (w != targetWidth || h != targetHeight);
 
        return ret;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession ses = request.getSession();
        try (PrintWriter out = response.getWriter()) {

            DBConnect db = new DBConnect();
            try {
                db.getConnection(); 
                if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("upload_photo")) {
                    Part filePart = request.getPart("myimg");
                    BufferedImage imBuff = getScaledInstance(ImageIO.read(filePart.getInputStream()), 150, 200, true);
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    ImageIO.write(imBuff, "jpg", os);
                    InputStream is = new ByteArrayInputStream(os.toByteArray());
                    PreparedStatement ps = null;
                    if (request.getParameter("user_type") != null && request.getParameter("user_type").equalsIgnoreCase("student")) {
                        ps = db.connection.prepareStatement("INSERT INTO camps.student_photo (   sp_id,   student_id,   photo,   STATUS,   inserted_by,   inserted_date,   updated_by,   updated_date ) VALUES   (     null,     '" + request.getParameter("user_id") + "',     ?,     1,     " + ses.getAttribute("userId") + ",     now(),     " + ses.getAttribute("userId") + ",     now()   ) ON DUPLICATE KEY UPDATE  photo=values(photo),status=1,updated_by=values(updated_by),updated_date=now(); ");
                    } else {
                        ps = db.connection.prepareStatement("INSERT INTO camps.staff_photo (   sp_id,   staff_id,   photo,   STATUS,   inserted_by,   inserted_date,   updated_by,   updated_date ) VALUES   (     null,     '" + request.getParameter("user_id") + "',     ?,     1,     " + ses.getAttribute("userId") + ",     now(),     " + ses.getAttribute("userId") + ",     now()   ) ON DUPLICATE KEY UPDATE  photo=values(photo),status=1,updated_by=values(updated_by),updated_date=now(); ");
                    }
                    ps.setBlob(1, is);
                    ps.execute();
                } else if (request.getParameter("option") != null && request.getParameter("option").equalsIgnoreCase("load_photo")) {
                    if (request.getParameter("user_type") != null && request.getParameter("user_type").equalsIgnoreCase("student")) {
                        db.read("SELECT IFNULL(CONCAT('<img src=\\'data:image/jpeg;base64,',TO_BASE64(sph.photo),'\\' width=\\'10%\\' height=\\'10%\\' alt=\\'User\\'/>'),CONCAT('<img src=\\'../../images/user.png\\'width=\\'48\\' height=\\'48\\' alt=\\'User\\'/>'))  img  FROM camps.student_photo sph WHERE sph.status>0 AND sph.student_id='" + request.getParameter("user_id") + "'");
                    } else {
                        db.read("SELECT IFNULL(CONCAT('<img src=\\'data:image/jpeg;base64,',TO_BASE64(sph.photo),'\\' width=\\'10%\\' height=\\'10%\\' alt=\\'User\\'/>'),CONCAT('<img src=\\'../../images/user.png\\'width=\\'48\\' height=\\'48\\' alt=\\'User\\'/>'))  img  FROM camps.staff_photo sph WHERE sph.status>0 AND sph.staff_id='" + request.getParameter("user_id") + "'");
                    }
                    if (db.rs.next()) {
                        out.print(db.rs.getString("img"));
                    } else {
                        out.print("<img src='../../images/user.png'width='48' height='48' alt='User'/>");
                    }
                }

            } catch (Exception ex) {
                out.print(ex);
            } finally {
                try {
                    db.closeConnection();
                } catch (SQLException ex) {
                    Logger.getLogger(image_process.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
