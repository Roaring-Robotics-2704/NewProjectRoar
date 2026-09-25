package frc.robot.subsystems.superstructure.indexer;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.littletonrobotics.junction.Logger;

public class Indexer extends SubsystemBase {
    private final IndexerIO indexerIO;

    public Indexer(IndexerIO indexerIO) {
        this.indexerIO = indexerIO;
    }


}