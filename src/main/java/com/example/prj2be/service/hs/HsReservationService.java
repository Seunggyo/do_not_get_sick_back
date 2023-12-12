package com.example.prj2be.service.hs;

import com.example.prj2be.domain.hs.Hs;
import com.example.prj2be.domain.hs.HsReservation;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.hs.HsReservationMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HsReservationService {

    private final HsReservationMapper mapper;


    public Hs get(Integer id) {
        return mapper.selectByBusinessId(id);
    }

    public boolean validate(HsReservation reservation) {
        if (reservation == null) {
            return false;
        }
        if (reservation.getBusinessId() == null || reservation.getBusinessId() < 1) {
            return false;
        }
        if (reservation.getReservationDate() == null) {
            return false;
        }
        if (reservation.getReservationHour() == null || reservation.getReservationMin() == null
            || reservation.getReservationHour() < 1) {
            return false;
        }
        return true;
    }

    public boolean add(HsReservation reservation, Member login) {
        reservation.setMemberId(login.getId());
        return mapper.insert(reservation) == 1;
    }

    public boolean isAccess(Integer id, Member login) {
        HsReservation reservation = mapper.selectById(id);
        if (login == null) {
            return false;
        }
        if (login.isAdmin()) {
            return true;
        }
        return reservation.getMemberId().equals(login.getId());
    }

    public boolean remove(Integer id) {
        return mapper.remove(id) == 1;
    }


    public List<HsReservation> reservationGet(String memberId) {
        return mapper.selectByMemberId(memberId);
    }

    public List<HsReservation> checkGet(String memberId) {
        return mapper.selectByCheckMemberId(memberId);
    }

    public List<HsReservation> businessGet(Integer memberId) {
        return mapper.selectByBusinessMemberId(memberId);
    }

    public List<Hs> memberToBList(String memberId) {
        return mapper.selectByMemberIdBList(memberId);
    }

    public Integer reservationOk(Integer reservationId) {
        return mapper.updateByReservationId(reservationId);
    }

    public List<HsReservation> bIdCheckGet(Integer businessId) {
        return mapper.bIdCheckGet(businessId);
    }
}
