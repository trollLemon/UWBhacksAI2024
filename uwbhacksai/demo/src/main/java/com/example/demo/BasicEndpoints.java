
import org.springframework.web.bind.annotation.GetMapping;

public class BasicEndpoints{
	@GetMapping("/error")
	public String error(){
		return "ERR";
	}

}
