import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * WebServer - PROVIDED. DO NOT EDIT.
 * Connects the web page (web/index.html) to the EmployeeManager backend.
 * It reads what the page sends, validates it, calls YOUR EmployeeManager
 * methods, and sends the answer back as plain text.
 *
 * Reply format:  line 1 = OK / ERR / NOTFOUND,  line 2 = message,  other lines = data
 */
public class WebServer implements HttpHandler {

    private File page;
    private EmployeeManager manager = new EmployeeManager();
    private AutoChecker checker = new AutoChecker();

    private WebServer(File page) {
        this.page = page;
    }

    // ------------------------------------------------------------
    // START
    // ------------------------------------------------------------
    public static void start() {
        File page = findPage();
        if (page == null) {
            System.out.println("ERROR: cannot find web/index.html.");
            System.out.println("Keep the 'web' folder next to the 'src' folder.");
            return;
        }

        WebServer handler = new WebServer(page);
        HttpServer server = null;
        int port = 8080;
        while (server == null && port <= 8090) {
            try {
                server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
            } catch (IOException e) {
                port++;
            }
        }
        if (server == null) {
            System.out.println("ERROR: no free port between 8080 and 8090.");
            return;
        }

        server.createContext("/", handler);
        server.start();

        String url = "http://localhost:" + port + "/";
        System.out.println("========================================");
        System.out.println("  EMPLOYEE RECORDS SYSTEM is running");
        System.out.println("  Open this address in your browser:");
        System.out.println("  " + url);
        System.out.println("  (Stop the program to shut the server down.)");
        System.out.println("========================================");
        try {
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
            }
        } catch (Throwable t) {
            // No browser could be opened automatically - open the address by hand.
        }
    }

    // Looks for web/index.html in the working folder, the class folder, and their parents.
    private static File findPage() {
        File found = searchUp(new File(System.getProperty("user.dir")));
        if (found != null) {
            return found;
        }
        try {
            File where = new File(WebServer.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            return searchUp(where);
        } catch (Throwable t) {
            return null;
        }
    }

    private static File searchUp(File dir) {
        for (int i = 0; i < 5 && dir != null; i++) {
            File f = new File(dir, "web" + File.separator + "index.html");
            if (f.exists()) {
                return f;
            }
            dir = dir.getParentFile();
        }
        return null;
    }

    // ------------------------------------------------------------
    // HANDLE EVERY REQUEST
    // ------------------------------------------------------------
    @Override
    public void handle(HttpExchange ex) throws IOException {
        String path = ex.getRequestURI().getPath();

        if (path.equals("/") || path.equals("/index.html")) {
            send(ex, 200, "text/html; charset=UTF-8", new String(Files.readAllBytes(page.toPath()), StandardCharsets.UTF_8));
            return;
        }
        if (!path.startsWith("/api/")) {
            send(ex, 404, "text/plain; charset=UTF-8", "Not found");
            return;
        }

        String reply;
        try {
            Map<String, String> p = readParams(ex);
            reply = handleApi(path, p);
        } catch (Throwable t) {
            // If YOUR code crashes, the page shows the error instead of the server dying.
            t.printStackTrace();
            reply = "ERR\nYour code crashed: " + t + "  (see the console for the full error)";
        }
        send(ex, 200, "text/plain; charset=UTF-8", reply);
    }

    private String handleApi(String path, Map<String, String> p) {
        if (path.equals("/api/list")) {
            StringBuilder sb = new StringBuilder("OK\n\n");
            for (int i = 0; i < manager.getEmployees().size(); i++) {
                sb.append(row(manager.getEmployees().get(i))).append("\n");
            }
            return sb.toString();
        }

        if (path.equals("/api/add")) {
            String problem = validate(p);
            if (problem != null) {
                return "ERR\n" + problem;
            }
            Employee r = new Employee(get(p, "id"), get(p, "name"), get(p, "department"), get(p, "position"), Double.parseDouble(get(p, "salary")));
            if (manager.addEmployee(r)) {
                return "OK\nEmployee " + r.getName() + " was added.";
            }
            return "ERR\nEmployee was NOT added (duplicate ID " + r.getEmployeeId() + ").";
        }

        if (path.equals("/api/search")) {
            String id = get(p, "id");
            if (!isValidId(id)) {
                return "ERR\nEmployee ID must look like EMP-001.";
            }
            Employee found = manager.searchById(id);
            if (found == null) {
                return "NOTFOUND\nEmployee with ID " + id + " was not found.";
            }
            Person person = found; // polymorphism: a Employee stored in a Person variable
            return "OK\nEmployee found.\n" + row(found) + "|" + person.getRole() + "\n";
        }

        if (path.equals("/api/update")) {
            String problem = validate(p);
            if (problem != null) {
                return "ERR\n" + problem;
            }
            boolean done = manager.updateEmployee(get(p, "id"), get(p, "name"), get(p, "department"), get(p, "position"), Double.parseDouble(get(p, "salary")));
            if (done) {
                return "OK\nEmployee " + get(p, "id") + " was updated.";
            }
            return "ERR\nEmployee " + get(p, "id") + " was not found, so nothing was updated.";
        }

        if (path.equals("/api/delete")) {
            String id = get(p, "id");
            if (manager.deleteEmployee(id)) {
                return "OK\nEmployee " + id + " was deleted.";
            }
            return "ERR\nEmployee " + id + " was not found, so nothing was deleted.";
        }

        if (path.equals("/api/sort")) {
            String a = get(p, "algo");
            String b = get(p, "by");
            if (!isWhole(a) || !isWhole(b)) {
                return "ERR\nChoose an algorithm and a sort field.";
            }
            int algo = Integer.parseInt(a);
            int by = Integer.parseInt(b);
            if (algo < 1 || algo > 5 || by < 1 || by > 5) {
                return "ERR\nInvalid algorithm or sort field.";
            }
            manager.sortEmployees(algo, by);
            return "OK\nSorted using " + manager.getAlgorithmName(algo) + " by " + manager.getSortFieldName(by) + " (ascending).";
        }

        if (path.equals("/api/reset")) {
            manager.resetSampleData();
            return "OK\nSample data loaded.";
        }

        if (path.equals("/api/clear")) {
            manager.clearAll();
            return "OK\nAll students removed.";
        }

        if (path.equals("/api/check")) {
            return checker.runAll();
        }

        return "ERR\nUnknown request.";
    }

    // ------------------------------------------------------------
    // HELPERS
    // ------------------------------------------------------------
    private String row(Employee s) {
        return s.getEmployeeId() + "|" + s.getName() + "|" + s.getDepartment() + "|" + s.getPosition() + "|" + String.format(Locale.US, "%.2f", s.getSalary());
    }

    private String get(Map<String, String> p, String key) {
        String v = p.get(key);
        if (v == null) {
            return "";
        }
        return v.trim();
    }

    private String validate(Map<String, String> p) {
        if (!isValidId(get(p, "id"))) {
            return "Employee ID must look like EMP-001.";
        }
        String name = get(p, "name");
        if (name.isEmpty() || name.indexOf('|') >= 0) {
            return "Name is required and cannot contain the | character.";
        }
        String v_department = get(p, "department");
        if (v_department.isEmpty() || v_department.indexOf('|') >= 0) {
            return "Department is required and cannot contain the | character.";
        }
        String v_position = get(p, "position");
        if (v_position.isEmpty() || v_position.indexOf('|') >= 0) {
            return "Position is required and cannot contain the | character.";
        }
        String v_salary = get(p, "salary");
        if (!isDecimal(v_salary) || Double.parseDouble(v_salary) < 0 || Double.parseDouble(v_salary) > 1000000) {
            return "Salary must be a number from 0 to 1000000.";
        }
        return null;
    }

    private boolean isValidId(String text) {
        if (text.length() != 7 || !text.startsWith("EMP-")) {
            return false;
        }
        for (int i = 4; i < text.length(); i++) {
            if (!Character.isDigit(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean isWhole(String text) {
        if (text.length() == 0 || text.length() > 9) {
            return false;
        }
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isDigit(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean isDecimal(String text) {
        if (text.length() == 0 || text.length() > 15) {
            return false;
        }
        int dots = 0;
        int digits = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '.') {
                dots++;
            } else if (Character.isDigit(c)) {
                digits++;
            } else {
                return false;
            }
        }
        return dots <= 1 && digits >= 1;
    }

    private Map<String, String> readParams(HttpExchange ex) throws IOException {
        Map<String, String> map = new HashMap<String, String>();
        parse(ex.getRequestURI().getRawQuery(), map);
        if (ex.getRequestMethod().equals("POST")) {
            InputStream in = ex.getRequestBody();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] chunk = new byte[1024];
            int n;
            while ((n = in.read(chunk)) != -1) {
                buffer.write(chunk, 0, n);
            }
            parse(new String(buffer.toByteArray(), StandardCharsets.UTF_8), map);
        }
        return map;
    }

    private void parse(String text, Map<String, String> map) throws IOException {
        if (text == null || text.isEmpty()) {
            return;
        }
        String[] pairs = text.split("&");
        for (int i = 0; i < pairs.length; i++) {
            String[] kv = pairs[i].split("=", 2);
            String key = URLDecoder.decode(kv[0], "UTF-8");
            String value = kv.length > 1 ? URLDecoder.decode(kv[1], "UTF-8") : "";
            map.put(key, value);
        }
    }

    private void send(HttpExchange ex, int code, String type, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", type);
        ex.getResponseHeaders().set("Cache-Control", "no-store");
        ex.sendResponseHeaders(code, bytes.length);
        OutputStream out = ex.getResponseBody();
        out.write(bytes);
        out.close();
    }
}
