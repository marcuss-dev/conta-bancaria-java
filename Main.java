import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

class Transacao {
    private LocalDateTime data;
    private String tipo;
    private double valor;
    private String descricao;

    public Transacao(String tipo, double valor, String descricao) {
        this.data = LocalDateTime.now();
        this.tipo = tipo;
        this.valor = valor;
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return data.format(formatter) + " | " + tipo + " | "
                + String.format(Locale.forLanguageTag("pt-BR"), "R$ %.2f", valor)
                + " | " + descricao;
    }
}

class ContaBancaria {
    private String titular;
    private int numeroConta;
    private double saldo;
    private double limite;
    private List<Transacao> extrato;
    private double taxaSaque = 2.50;
    private static final double TAXA_TRANSFERENCIA = 1.50;

    public ContaBancaria(String titular, int numeroConta, double saldoInicial, double limite) {
        this.titular = titular;
        this.numeroConta = numeroConta;
        this.saldo = saldoInicial;
        this.limite = limite;
        this.extrato = new ArrayList<>();
        this.extrato.add(new Transacao("ABERTURA", saldoInicial, "Abertura de conta"));
    }

    public void depositar(double valor) {
        if (valor <= 0) {
            System.out.println("Valor de depósito inválido!");
            return;
        }

        saldo += valor;
        extrato.add(new Transacao("DEPÓSITO", valor, "Depósito em conta"));
        System.out.println("Depósito realizado com sucesso!");
    }

    public void sacar(double valor) {
        if (valor <= 0) {
            System.out.println("Valor de saque inválido!");
            return;
        }

        double valorTotal = valor + taxaSaque;

        if (valorTotal <= saldo + limite) {
            saldo -= valorTotal;
            extrato.add(new Transacao("SAQUE", valorTotal, "Saque com taxa de "
                    + String.format(Locale.forLanguageTag("pt-BR"), "R$ %.2f", taxaSaque)));
            System.out.println("Saque realizado com sucesso!");
        } else {
            System.out.println("Saldo e limite insuficientes para esse saque!");
        }
    }

    public boolean transferir(ContaBancaria destino, double valor) {
        if (destino == null) {
            System.out.println("Conta de destino inválida!");
            return false;
        }

        if (valor <= 0) {
            System.out.println("Valor de transferência inválido!");
            return false;
        }

        double valorTotal = valor + TAXA_TRANSFERENCIA;

        if (valorTotal <= saldo + limite) {
            saldo -= valorTotal;
            destino.saldo += valor;
            destino.extrato.add(new Transacao("TRANSFERÊNCIA RECEBIDA", valor,
                    "Recebido de conta " + this.numeroConta));
            extrato.add(new Transacao("TRANSFERÊNCIA ENVIADA", valorTotal,
                    "Transferência para conta " + destino.numeroConta));
            System.out.println("Transferência realizada com sucesso!");
            return true;
        }

        System.out.println("Saldo insuficiente para transferir!");
        return false;
    }

    public void pagarConta(String nomeServico, double valor) {
        if (valor <= 0) {
            System.out.println("Valor inválido para pagamento!");
            return;
        }

        if (valor <= saldo + limite) {
            saldo -= valor;
            extrato.add(new Transacao("PAGAMENTO", valor, "Pagamento de " + nomeServico));
            System.out.println("Pagamento realizado com sucesso!");
        } else {
            System.out.println("Saldo insuficiente para pagar essa conta!");
        }
    }

    public void aplicarRendimento(double percentual) {
        if (percentual <= 0 || percentual > 100) {
            System.out.println("Percentual de rendimento inválido!");
            return;
        }

        double valorRendimento = saldo * (percentual / 100);
        saldo += valorRendimento;
        extrato.add(new Transacao("RENDIMENTO", valorRendimento,
                "Aplicação de " + percentual + "% sobre o saldo"));
        System.out.println("Rendimento aplicado com sucesso!");
    }

    public void mostrarSaldo() {
        System.out.println("\n=== DADOS DA CONTA ===");
        System.out.println("Titular: " + titular);
        System.out.println("Número da conta: " + numeroConta);
        System.out.println("Saldo atual: " + String.format(Locale.forLanguageTag("pt-BR"), "R$ %.2f", saldo));
        System.out.println("Limite disponível: " + String.format(Locale.forLanguageTag("pt-BR"), "R$ %.2f", limite));
    }

    public void exibirExtrato() {
        if (extrato.isEmpty()) {
            System.out.println("Nenhuma transação registrada.");
            return;
        }

        System.out.println("\n=== EXTRATO ===");
        for (Transacao transacao : extrato) {
            System.out.println(transacao);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Nome do titular da conta principal: ");
        String titular = sc.nextLine();

        ContaBancaria conta = new ContaBancaria(titular, 1001, 1500.00, 800.00);
        ContaBancaria contaDestino = new ContaBancaria("Maria Silva", 2002, 2200.00, 500.00);

        int opcao;

        do {
            System.out.println("\n=== BANCO JAVA ===");
            System.out.println("1 - Depositar");
            System.out.println("2 - Sacar");
            System.out.println("3 - Transferir");
            System.out.println("4 - Pagar conta");
            System.out.println("5 - Ver saldo");
            System.out.println("6 - Exibir extrato");
            System.out.println("7 - Aplicar rendimento");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = sc.nextInt();

            switch (opcao) {
                case 1:
                    System.out.print("Valor do depósito: R$ ");
                    double deposito = sc.nextDouble();
                    conta.depositar(deposito);
                    break;
                case 2:
                    System.out.print("Valor do saque: R$ ");
                    double saque = sc.nextDouble();
                    conta.sacar(saque);
                    break;
                case 3:
                    System.out.print("Valor da transferência: R$ ");
                    double valorTransferencia = sc.nextDouble();
                    conta.transferir(contaDestino, valorTransferencia);
                    break;
                case 4:
                    System.out.print("Nome do serviço ou conta: ");
                    sc.nextLine();
                    String nomeServico = sc.nextLine();
                    System.out.print("Valor do pagamento: R$ ");
                    double valorPagamento = sc.nextDouble();
                    conta.pagarConta(nomeServico, valorPagamento);
                    break;
                case 5:
                    conta.mostrarSaldo();
                    break;
                case 6:
                    conta.exibirExtrato();
                    break;
                case 7:
                    System.out.print("Percentual de rendimento: ");
                    double percentual = sc.nextDouble();
                    conta.aplicarRendimento(percentual);
                    break;
                case 0:
                    System.out.println("Obrigado por usar o Banco Java!");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);

        sc.close();
    }
}
