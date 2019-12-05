package projekti.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import projekti.entities.Blocked;
import projekti.entities.Following;
import projekti.entities.Profile;
import projekti.listobjects.DataPacket;

@Service
public class DataPacketServices {
    public DataPacket convertProfileAndLocalDateTimeToDataPacket(Profile profile, LocalDateTime localDateTime) {
        return new DataPacket(profile.getName(), profile.getAlias(), localDateTime, "");
    }
    
    public List<DataPacket> convertFollowerListToDataPackets(List<Following> followings) {
        List<DataPacket> whoIamFollowingDataPackets = new ArrayList<>();
        for (Following following : followings) {            
            DataPacket dataPacket = convertProfileAndLocalDateTimeToDataPacket(following.getFollowed(), following.getLocalDateTime());
            whoIamFollowingDataPackets.add(dataPacket);
        }
        return whoIamFollowingDataPackets;
    }
    
    public List<DataPacket> convertFollowedListToDataPackets(List<Following> followings) {
        List<DataPacket> whoAreFollowingMeDataPackets = new ArrayList<>();
        for (Following following : followings) {
            DataPacket dataPacket = convertProfileAndLocalDateTimeToDataPacket(following.getFollower(), following.getLocalDateTime());
            whoAreFollowingMeDataPackets.add(dataPacket);
        }
        return whoAreFollowingMeDataPackets;
    }
    
    public List<DataPacket> convertBlockedListToDataPackets(List<Blocked> blockedList) {
        List<DataPacket> blockedDataPackets = new ArrayList<>();
        for (Blocked blocked : blockedList) {            
            DataPacket dataPacket = convertProfileAndLocalDateTimeToDataPacket(blocked.getBlocked(), blocked.getLocalDateTime());
            blockedDataPackets.add(dataPacket);
        }
        return blockedDataPackets;
    }
    
    public List<DataPacket> convertBlockerListToDataPackets(List<Blocked> blockerList) {
        List<DataPacket> blockedDataPackets = new ArrayList<>();
        for (Blocked blocked : blockerList) {            
            DataPacket dataPacket = convertProfileAndLocalDateTimeToDataPacket(blocked.getBlocker(), blocked.getLocalDateTime());
            blockedDataPackets.add(dataPacket);
        }
        return blockedDataPackets;
    }
}
