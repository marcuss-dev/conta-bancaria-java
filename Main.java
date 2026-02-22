import java.util.Scanner;

class ContaBancaria {
    private double saldo;

    public ContaBancaria(double saldoInicial) {
        this.saldo = saldoInicial;
    }

    public void depositar(double valor) {
        saldo += valor;
        System.out.println("Depósito realizado!");
    }

    public void sacar(double valor) {
        if (valor <= saldo) {
            saldo -= valor;
            System.out.println("Saque realizado!");
        } else {
            System.out.println("Saldo insuficiente!");
        }
    }

    public void mostrarSaldo() {
        System.out.println("Saldo atual: R$ " + saldo);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ContaBancaria conta = new ContaBancaria(1000);

        int opcao;

        do {
            System.out.println("\n1 - Depositar");
            System.out.println("2 - Sacar");
            System.out.println("3 - Ver Saldo");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");
            opcao = sc.nextInt();

            if (opcao == 1) {
                System.out.print("Valor: ");
                double valor = sc.nextDouble();
                conta.depositar(valor);
            } else if (opcao == 2) {
                System.out.print("Valor: ");
                double valor = sc.nextDouble();
                conta.sacar(valor);
            } else if (opcao == 3) {
                conta.mostrarSaldo();
            }

        } while (opcao != 0);

        sc.close();
    }
}
