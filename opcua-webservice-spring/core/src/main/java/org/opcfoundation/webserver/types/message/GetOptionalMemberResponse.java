package org.opcfoundation.webserver.types.message;

import java.util.Set;

public class GetOptionalMemberResponse {
    private final Set<String> absentMembers;

    public GetOptionalMemberResponse(Set<String> absentMembers)
    {
        this.absentMembers = absentMembers;
    }

    public Set<String> getAbsentMembers() {
        return absentMembers;
    }
}
