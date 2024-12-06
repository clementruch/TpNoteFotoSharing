package local.epul4a.tpnotefotosharing.controller;

import local.epul4a.tpnotefotosharing.model.Photo;
import local.epul4a.tpnotefotosharing.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PhotoController {
    @Autowired
    private PhotoRepository photoRepository;

    @GetMapping("/Photo")
    public String index(Model model){
        model.addAttribute("photos",photoRepository.findAll());
        return null;
    }
    @PostMapping("/addPhoto")
    public String addPhoto(String title,String url,String description){
        Photo photo=new Photo();
        photo.setTitle(title);
        return title;
    }
}
