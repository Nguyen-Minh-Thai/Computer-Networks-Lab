public class PrimeRun implements Runnable {
    long minPrime;

    PrimeRun(long minPrime) {
        this.minPrime = minPrime;
    }

    public void run() {
        // Tìm các số nguyên tố lớn hơn minPrime, dừng khi bị interrupt
        for (long n = minPrime + 1; !Thread.currentThread().isInterrupted(); n++) {
            if (isPrime(n)) System.out.println(Thread.currentThread().getName() + ": " + n);
        }
        System.out.println(Thread.currentThread().getName() + " stopped.");
    }

    static boolean isPrime(long n) {
        if (n < 2) return false;
        for (long i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        // Chạy 2 luồng cùng lúc với 2 mốc khác nhau
        Thread t1 = new Thread(new PrimeRun(143), "T1");
        Thread t2 = new Thread(new PrimeRun(1000), "T2");
        t1.start();
        t2.start();

        Thread.sleep(20);   // cho 2 luồng chạy một lúc
        t1.interrupt();     // rồi dừng cả hai
        t2.interrupt();
    }
}