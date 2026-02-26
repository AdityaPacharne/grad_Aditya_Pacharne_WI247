# Jenkins + GitHub Webhook + Java Calculator Project

This project demonstrates how to integrate **Jenkins**, **GitHub Webhooks**, and **ngrok** to automatically trigger a Jenkins job whenever code is pushed to a GitHub repository.

---

## Step 1: Install and Run ngrok

1. Install ngrok from https://ngrok.com/
2. Run the following command:

```bash
ngrok http 8080
```
<img width="1411" height="363" alt="Screenshot 2026-02-26 131442" src="https://github.com/user-attachments/assets/29cb0679-70ce-4365-8f02-306012762791" />

This will expose your local Jenkins server (running on port 8080) to the internet.

3. Copy the generated **public URL** (e.g., `https://random-id.ngrok.io`).

---

## Step 2: Create GitHub Repository

1. Go to GitHub.
2. Create a new repository named "calculator"

---

## Step 3: Configure GitHub Webhook

1. Open your `calculator` repository.
2. Go to **Settings → Webhooks**.
3. Click **Add webhook**.
4. In **Payload URL**, paste:
5. Keep all other settings as default.
6. Click **Add webhook**.

<img width="961" height="1044" alt="Screenshot 2026-02-26 131654" src="https://github.com/user-attachments/assets/48417e09-1924-4b88-88dd-a36acbdeb05b" />

---

## Step 4: Clone Repository Locally

Clone the repository:

```bash
git clone <your-repo-url>
cd calculator
```

---

## Step 5: Add Java Files

Create two files:

### Calculator.java

```java
class Calculator {
    public int add(int a, int b) { return a + b; }
    public int sub(int a, int b) { return a - b; }
    public int mul(int a, int b) { return a * b; }
    public int div(int a, int b) { return a / b; }
}
```

---

### CalculatorMain.java

```java
public class CalculatorMain {
    public static void main (String[] args) {
        Calculator c = new Calculator();
        System.out.println(c.add(6, 2));
        System.out.println(c.sub(6, 2));
        System.out.println(c.mul(6, 2));
        System.out.println(c.div(6, 2));
    }
}
```

---

## Step 6: Start Jenkins

Run Jenkins using:

```bash
java -jar jenkins.war
```

Then open in browser:

```
http://localhost:8080
```

---

## Step 7: Create Jenkins Freestyle Project

1. Click **New Item**.
2. Enter a name.
3. Select **Freestyle Project**.
4. Click **OK**.

### Configure the Project

#### Source Code Management
- Select **Git**
- Add your repository URL

<img width="1297" height="654" alt="Screenshot 2026-02-26 131930" src="https://github.com/user-attachments/assets/1cd12ca6-a569-4cef-851d-76f122515f05" />

#### Build Triggers
- Tick:  
  **GitHub hook trigger for GITScm polling**

<img width="980" height="343" alt="Screenshot 2026-02-26 132001" src="https://github.com/user-attachments/assets/2651da34-a64a-4140-ac32-b7214419b2fb" />

#### Build
- Add **Build Step**
- Choose **Execute Windows batch command**

Example command:

```bat
javac Calculator.java CalculatorMain.java
java CalculatorMain
```

<img width="1286" height="515" alt="Screenshot 2026-02-26 132052" src="https://github.com/user-attachments/assets/c0c29093-673d-4d87-ba71-10ed8702a0b6" />

5. Click **Save**.

---

## Final Result

Now, every time you:

```bash
git add .
git commit -m "update"
git push
```

GitHub will:
- Send a webhook POST request to ngrok
- ngrok forwards it to Jenkins
- Jenkins automatically triggers the job
- Java files are compiled and executed

<img width="435" height="160" alt="Screenshot 2026-02-26 132145" src="https://github.com/user-attachments/assets/dc0f9733-e9df-471a-86dc-63d2d0be8727" />

<img width="783" height="377" alt="Screenshot 2026-02-26 132307" src="https://github.com/user-attachments/assets/c07f5029-f864-4e14-860f-8bd95fe039f5" />

<img width="1360" height="873" alt="Screenshot 2026-02-26 132349" src="https://github.com/user-attachments/assets/8c70af61-b5ea-41b2-bd0c-c59547810120" />

Your CI pipeline is now working successfully!
