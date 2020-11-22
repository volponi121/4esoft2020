package aula20201116;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import aula20201109.Job;
import org.springframework.boot.web.embedded.netty.NettyRouteProvider;

import static java.lang.Thread.sleep;


public class App extends JDialog {
    private JobQueue jobs = new JobQueue();
    private List<JobConsumer> consumers = new ArrayList<>();
    private List<JobProducer> producers = new ArrayList<>();
    private JProgressBar progressBar = new JProgressBar(0);


    public static void main(String[] args) {        
        App app = new App();
        app.setSize(400,250);
        app.setVisible(true);
    }

    public App() {
        super();
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.add(createPanel());
    }


    private JPanel createPanel() {
        final JPanel panel = layoutPrincipal();

        final JPanel firstRowPanel = layoutProducers();

        final JPanel secondRowPanel = layoutConsumers();

        final JPanel thirdRowPanel = layoutCount(Color.green, "Job count:    ");

        final JTextField fieldJobCount = new JTextField(40);
        fieldJobCount.setEnabled(false);
        fieldJobCount.setMaximumSize(fieldJobCount.getPreferredSize());
        thirdRowPanel.add(fieldJobCount);
        thirdRowPanel.add(Box.createHorizontalGlue());

        //Registrando o listener de nosso padrão Observer para atualizar a UI quando o tamanho da
        //fila de jobs mudar (tanto para mais quanto para menos).
        registerListener(fieldJobCount, panel);

        panel.add(firstRowPanel);
        panel.add(secondRowPanel);
        panel.add(thirdRowPanel);

        return panel;
    }

    private JPanel layoutCount(Color green, String s) {
        final JPanel thirdRowPanel = new JPanel();
        thirdRowPanel.setLayout(new BoxLayout(thirdRowPanel, BoxLayout.X_AXIS));
        thirdRowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        thirdRowPanel.setBackground(green);
        thirdRowPanel.add(new JLabel(s));
        return thirdRowPanel;
    }


    private JPanel layoutConsumers() {
        final JPanel secondRowPanel = layoutCount(Color.red, "Consumers: ");
        final JTextField fieldConsumerCount = new JTextField(40);
        Font font = new Font("Courier", Font.BOLD,12);
        fieldConsumerCount.setFont(font);
        final JButton btnAddConsumer = new JButton(" + ");
        btnAddConsumer.addActionListener(e -> {
            JobConsumer newConsumer = new JobConsumer(jobs);
            consumers.add(newConsumer);
            fieldConsumerCount.setText(String.valueOf(consumers.size()));
            newConsumer.start();
        });
        fieldConsumerCount.setEnabled(false);
        fieldConsumerCount.setMaximumSize(fieldConsumerCount.getPreferredSize());
        btnAddConsumer.setMaximumSize(btnAddConsumer.getPreferredSize());
        secondRowPanel.add(fieldConsumerCount);
        secondRowPanel.add(btnAddConsumer);
        secondRowPanel.add(Box.createHorizontalGlue());
        return secondRowPanel;
    }

    private JPanel layoutProducers() {
        final JPanel firstRowPanel = layoutCount(Color.GRAY, "Producers:   ");
        final JTextField fieldProducerCount = new JTextField(40);
        final JButton btnAddProducer = new JButton(" + ");
        btnAddProducer.addActionListener(e -> {
            JobProducer newProducer = new JobProducer(jobs);
            producers.add(newProducer);
            fieldProducerCount.setText(String.valueOf(producers.size()));
            newProducer.start();
        });
        fieldProducerCount.setEnabled(false);
        fieldProducerCount.setMaximumSize(fieldProducerCount.getPreferredSize());
        btnAddProducer.setMaximumSize(btnAddProducer.getPreferredSize());
        firstRowPanel.add(fieldProducerCount);
        firstRowPanel.add(btnAddProducer);
        firstRowPanel.add(Box.createHorizontalGlue());
        return firstRowPanel;
    }

    private JPanel layoutPrincipal() {
        final JPanel panel = new JPanel();
        panel.setBackground(Color.blue);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createRaisedBevelBorder());
        return panel;
    }

    private void registerListener(JTextField fieldJobCount, JPanel panel) {
        this.jobs.addJobQueueListener(jobCount -> {
           createNewJob(jobCount, panel);
           fieldJobCount.setText(String.valueOf(jobCount));
        });
    }

    protected void createNewJob(int size, JPanel panel) throws InterruptedException {
        Job newJob = new Job(size);
        JobProgressPanel jobProgressPanel = new JobProgressPanel(newJob);
        jobProgressPanel.setBackground(Color.GRAY);
        panel.add(jobProgressPanel);
        panel.revalidate();
        sleep(3000);
        panel.remove(jobProgressPanel);
    }

    private static class JobProgressPanel extends JPanel {
        private Job job;
        private int wordDone = 0;
        private JProgressBar progressBar;

        public JobProgressPanel(Job job) {
            this.progressBar = new JProgressBar(job.getSize());
            this.job = job;
            BoxLayout boxLayout = new BoxLayout(this, BoxLayout.LINE_AXIS);
            this.setLayout(boxLayout);
            this.add(progressBar);
        }

    }


}
