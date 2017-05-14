package com.sh.controller;

import com.sh.dao.ProductDAO;
import com.sh.dao.ProductDAO;
import com.sh.dao.impl.ProductDAOImpl;
import com.sh.dao.impl.ProductDAOImpl;
import com.sh.model.Product;
import com.sh.model.Product;
import com.sh.model.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by tao on 2017/5/15 0015.
 */
@RestController
@RequestMapping("/api/product")
public class ProductController {
    private ProductDAO productDao;

    public ProductController() {
        productDao = new ProductDAOImpl();
    }

    @RequestMapping("/add")
    @ResponseBody
    public ModelAndView add(HttpServletRequest request, HttpSession session) {
        ModelAndView view = new ModelAndView();
        Product product = new Product();
        //设置uid
        User user = (User) session.getAttribute("user");
        Integer uid = new Integer(0);
        if (user == null || user.getId() == null) {
            //用户未登录,跳转到登录页面
            session.setAttribute("msg", "您还未登录,请登录后再发布商品,3秒后自动跳转到登录页面");
            session.setAttribute("autoReturn", "login.jsp");
            view.setViewName("redirect:/info.jsp");
            return view;
        } else {
            //用户已登录,可以发布商品
            uid = user.getId();
            String pName = request.getParameter("pName");
            Integer kId = Integer.parseInt(request.getParameter("kindId"));
            String pDesc = request.getParameter("pDesc");
            Integer pNum = Integer.parseInt(request.getParameter("pNum"));
            String pImage = "img/show/sanxing.jpg";
            Integer originPrice = Integer.parseInt(request.getParameter("originPrice"));
            Integer realPrice = Integer.parseInt(request.getParameter("realPrice"));

            product.setUid(uid);
            product.setPname(pName);
            product.setKid(kId);
            product.setPdesc(pDesc);
            product.setPnum(pNum);
            product.setPimage(pImage);
            product.setOriginprice(originPrice);
            product.setRealprice(realPrice);

            boolean result = (1 == productDao.insert(product));

            if (result == true) {
                session.setAttribute("msg", "发布成功,3秒后自动跳转到发布商品页面");
                session.setAttribute("autoReturn", "user_product.jsp");
            } else {
                session.setAttribute("msg", "发布失败,3秒后自动跳转到发布商品页面");
                session.setAttribute("autoReturn", "user_product.jsp");
            }
            view = new ModelAndView();
            view.setViewName("redirect:/info.jsp");
            return view;
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ModelAndView delete(HttpServletRequest request, HttpSession session) {
        ModelAndView view = new ModelAndView();
        Product product = new Product();
        //设置uid
        User user = (User) session.getAttribute("user");
        Integer uid = new Integer(0);
        if (user == null || user.getId() == null) {
            //用户未登录,跳转到登录页面
            session.setAttribute("msg", "您还未登录,3秒后自动跳转到登录页面");
            session.setAttribute("autoReturn", "login.jsp");
            view.setViewName("redirect:/info.jsp");
            return view;
        } else {
            //用户已登录,可以删除购物车
            Integer productId = Integer.parseInt(request.getParameter("id"));

            boolean result = (1 == productDao.delete(productId));

            if (result == true) {
                session.setAttribute("msg", "删除发布商品成功,3秒后自动跳转到发布商品页面");
                session.setAttribute("autoReturn", "user_product.jsp");
            } else {
                session.setAttribute("msg", "删除发布商品失败,3秒后自动跳转到发布商品页面");
                session.setAttribute("autoReturn", "user_product.jsp");
            }
            view = new ModelAndView();
            view.setViewName("redirect:/info.jsp");
            return view;
        }
    }

    @RequestMapping("/search")
    @ResponseBody
    public ModelAndView search(HttpServletRequest request, HttpSession session) {
        ModelAndView view = new ModelAndView();
        List<Product> productList;
        String pName = request.getParameter("name");

        ProductDAO pd = new ProductDAOImpl();
        productList = pd.getProductByLikeName(pName);

        session.setAttribute("productlist",productList);

        view = new ModelAndView();
        view.setViewName("redirect:/search.jsp");
        return view;
    }

}
